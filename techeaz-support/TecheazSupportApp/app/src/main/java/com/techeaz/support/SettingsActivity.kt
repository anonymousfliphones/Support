package com.techeaz.support

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Settings activity for configuring app preferences
 * This is a placeholder implementation - you can expand it later
 */
class SettingsActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // For now, just show a simple message
        // In a full implementation, you would create a proper settings UI
        // with preferences for GitHub URL, refresh intervals, etc.
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        
        // TODO: Implement settings UI
        // You can add PreferenceFragmentCompat or custom settings layout here
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}