package com.techeaz.support.adapters

import android.animation.ObjectAnimator
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.techeaz.support.R
import com.techeaz.support.data.FAQ
import com.techeaz.support.databinding.ItemFaqBinding

/**
 * RecyclerView adapter for displaying FAQ items with expandable functionality
 */
class FaqAdapter(
    private val onFaqClick: (FAQ) -> Unit
) : ListAdapter<FAQ, FaqAdapter.FaqViewHolder>(FaqDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(binding, onFaqClick)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FaqViewHolder(
        private val binding: ItemFaqBinding,
        private val onFaqClick: (FAQ) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var isExpanded = false
        private var currentFaq: FAQ? = null

        init {
            binding.faqHeader.setOnClickListener {
                currentFaq?.let { faq ->
                    toggleExpansion()
                    onFaqClick(faq)
                }
            }

            binding.btnHelpfulYes.setOnClickListener {
                currentFaq?.let { faq ->
                    Toast.makeText(binding.root.context, "Thanks for your feedback!", Toast.LENGTH_SHORT).show()
                    // In a real app, you might want to send this feedback to a server
                }
            }

            binding.btnHelpfulNo.setOnClickListener {
                currentFaq?.let { faq ->
                    Toast.makeText(binding.root.context, "Thanks for letting us know. We'll work to improve this answer.", Toast.LENGTH_LONG).show()
                    // In a real app, you might want to send this feedback to a server
                }
            }
        }

        fun bind(faq: FAQ) {
            currentFaq = faq
            
            // Set category badge
            binding.tvCategory.text = getCategoryDisplayName(faq.category)
            binding.tvCategory.setBackgroundColor(getCategoryColor(faq.category))
            
            // Set question
            binding.tvQuestion.text = faq.question
            
            // Set answer
            binding.tvAnswer.text = faq.answer
            
            // Set tags
            binding.chipGroupTags.removeAllViews()
            faq.tags.forEach { tag ->
                val chip = Chip(binding.root.context).apply {
                    text = "#$tag"
                    textSize = 12f
                    setChipBackgroundColorResource(R.color.primary)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    isClickable = false
                }
                binding.chipGroupTags.addView(chip)
            }
            
            // Set last updated
            binding.tvLastUpdated.text = "Updated: ${faq.getFormattedLastUpdated()}"
            
            // Reset expansion state
            isExpanded = false
            binding.faqContent.visibility = View.GONE
            binding.ivExpandIcon.rotation = 0f
        }

        private fun toggleExpansion() {
            isExpanded = !isExpanded
            
            if (isExpanded) {
                // Expand
                binding.faqContent.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(binding.ivExpandIcon, "rotation", 0f, 180f).apply {
                    duration = 200
                    start()
                }
            } else {
                // Collapse
                binding.faqContent.visibility = View.GONE
                ObjectAnimator.ofFloat(binding.ivExpandIcon, "rotation", 180f, 0f).apply {
                    duration = 200
                    start()
                }
            }
        }

        private fun getCategoryDisplayName(categoryId: String): String {
            return when (categoryId) {
                "general" -> "❓ General"
                "account" -> "👤 Account & Billing"
                "technical" -> "🔧 Technical"
                "getting-started" -> "🚀 Getting Started"
                else -> "📋 Other"
            }
        }

        private fun getCategoryColor(categoryId: String): Int {
            val context = binding.root.context
            return when (categoryId) {
                "general" -> ContextCompat.getColor(context, R.color.category_general)
                "account" -> ContextCompat.getColor(context, R.color.category_account)
                "technical" -> ContextCompat.getColor(context, R.color.category_technical)
                "getting-started" -> ContextCompat.getColor(context, R.color.category_getting_started)
                else -> ContextCompat.getColor(context, R.color.secondary)
            }
        }
    }

    class FaqDiffCallback : DiffUtil.ItemCallback<FAQ>() {
        override fun areItemsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
            return oldItem == newItem
        }
    }
}