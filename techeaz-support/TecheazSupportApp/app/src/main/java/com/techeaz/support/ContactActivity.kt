package com.techeaz.support

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Contact activity for detailed contact information
 * This is a placeholder implementation
 */
class ContactActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Contact Support"
        
        // TODO: Implement detailed contact UI
        // This could show detailed contact information, contact form, etc.
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}