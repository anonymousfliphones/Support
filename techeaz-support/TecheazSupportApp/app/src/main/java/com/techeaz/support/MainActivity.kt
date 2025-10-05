package com.techeaz.support

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.techeaz.support.adapters.AnnouncementAdapter
import com.techeaz.support.adapters.FaqAdapter
import com.techeaz.support.adapters.QuickActionAdapter
import com.techeaz.support.data.Category
import com.techeaz.support.data.FAQ
import com.techeaz.support.data.SupportContent
import com.techeaz.support.databinding.ActivityMainBinding
import com.techeaz.support.network.SupportRepository
import com.techeaz.support.viewmodels.MainViewModel

/**
 * Main activity for the Techeaz Support app
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    
    private lateinit var faqAdapter: FaqAdapter
    private lateinit var quickActionAdapter: QuickActionAdapter
    private lateinit var announcementAdapter: AnnouncementAdapter
    
    private var supportContent: SupportContent? = null
    private var filteredFaqs: List<FAQ> = emptyList()
    private var selectedCategory: String = "all"
    private var searchQuery: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerViews()
        setupViewModel()
        setupSearchView()
        setupFab()
        
        // Load content
        viewModel.loadSupportContent()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }
    
    private fun setupRecyclerViews() {
        // FAQ RecyclerView
        faqAdapter = FaqAdapter { faq ->
            // Handle FAQ item click (expand/collapse)
            // This will be handled by the adapter internally
        }
        binding.rvFaqs.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = faqAdapter
        }
        
        // Quick Actions RecyclerView
        quickActionAdapter = QuickActionAdapter { quickAction ->
            handleQuickAction(quickAction.action)
        }
        binding.rvQuickActions.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = quickActionAdapter
        }
        
        // Announcements RecyclerView
        announcementAdapter = AnnouncementAdapter()
        binding.rvAnnouncements.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = announcementAdapter
        }
    }
    
    private fun setupViewModel() {
        val repository = SupportRepository()
        viewModel = ViewModelProvider(this, MainViewModel.Factory(repository))[MainViewModel::class.java]
        
        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
        
        // Observe support content
        viewModel.supportContent.observe(this) { content ->
            content?.let {
                this.supportContent = it
                updateUI(it)
            }
        }
        
        // Observe error messages
        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                showError(it)
            }
        }
    }
    
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchQuery = it
                    filterFaqs()
                }
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText ?: ""
                filterFaqs()
                return true
            }
        })
    }
    
    private fun setupFab() {
        binding.fabRefresh.setOnClickListener {
            viewModel.refreshContent()
        }
    }
    
    private fun updateUI(content: SupportContent) {
        // Update app description
        binding.tvAppDescription.text = content.app.description
        
        // Update quick actions
        quickActionAdapter.submitList(content.quickActions)
        
        // Update announcements
        val activeAnnouncements = content.announcements.filter { it.active }
        if (activeAnnouncements.isNotEmpty()) {
            binding.cardAnnouncements.visibility = View.VISIBLE
            announcementAdapter.submitList(activeAnnouncements)
        } else {
            binding.cardAnnouncements.visibility = View.GONE
        }
        
        // Setup categories
        setupCategories(content.categories)
        
        // Update FAQs
        filteredFaqs = content.faqs
        filterFaqs()
        
        // Setup contact buttons
        setupContactButtons(content.contact)
    }
    
    private fun setupCategories(categories: List<Category>) {
        binding.chipGroupCategories.removeAllViews()
        
        // Add "All Categories" chip
        val allChip = Chip(this).apply {
            text = getString(R.string.categories_all)
            isCheckable = true
            isChecked = true
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedCategory = "all"
                    filterFaqs()
                }
            }
        }
        binding.chipGroupCategories.addView(allChip)
        
        // Add category chips
        categories.forEach { category ->
            val chip = Chip(this).apply {
                text = "${category.icon} ${category.name}"
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedCategory = category.id
                        filterFaqs()
                    }
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }
    
    private fun setupContactButtons(contact: com.techeaz.support.data.ContactInfo) {
        binding.btnContactEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${contact.email}")
                putExtra(Intent.EXTRA_SUBJECT, "Support Request")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.btnContactPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${contact.phone}")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No phone app found", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun filterFaqs() {
        val content = supportContent ?: return
        
        var filtered = content.faqs
        
        // Filter by category
        if (selectedCategory != "all") {
            filtered = filtered.filter { it.category == selectedCategory }
        }
        
        // Filter by search query
        if (searchQuery.isNotEmpty()) {
            filtered = filtered.filter { it.matchesSearch(searchQuery) }
        }
        
        filteredFaqs = filtered
        updateFaqList()
    }
    
    private fun updateFaqList() {
        if (filteredFaqs.isEmpty()) {
            binding.layoutNoResults.visibility = View.VISIBLE
            binding.rvFaqs.visibility = View.GONE
        } else {
            binding.layoutNoResults.visibility = View.GONE
            binding.rvFaqs.visibility = View.VISIBLE
            faqAdapter.submitList(filteredFaqs)
        }
        
        // Update results count
        val count = filteredFaqs.size
        val text = if (count == 1) "1 article found" else "$count articles found"
        binding.tvResultsCount.text = text
    }
    
    private fun handleQuickAction(action: String) {
        when {
            action.startsWith("mailto:") -> {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(action))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
                }
            }
            action.startsWith("tel:") -> {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(action))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No phone app found", Toast.LENGTH_SHORT).show()
                }
            }
            action.startsWith("http") -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(action))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No browser found", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "Action: $action", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun showLoading() {
        binding.layoutLoading.visibility = View.VISIBLE
        binding.rvFaqs.visibility = View.GONE
        binding.layoutNoResults.visibility = View.GONE
    }
    
    private fun hideLoading() {
        binding.layoutLoading.visibility = View.GONE
    }
    
    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                viewModel.loadSupportContent()
            }
            .show()
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.refreshContent()
                true
            }
            R.id.action_settings -> {
                // Open settings activity
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}