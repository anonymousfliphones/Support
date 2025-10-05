package com.techeaz.support

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

/**
 * Splash screen activity that displays the app logo/branding before launching MainActivity
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    
    companion object {
        private const val SPLASH_DELAY_MS = 2000L // 2 seconds
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // The splash screen background is set via the theme
        // No need to set content view as the drawable handles the UI
        
        // Delay and then launch MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startMainActivity()
        }, SPLASH_DELAY_MS)
    }
    
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        
        // Optional: Add transition animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    
    override fun onBackPressed() {
        // Disable back button during splash screen
        // Do nothing
    }
}