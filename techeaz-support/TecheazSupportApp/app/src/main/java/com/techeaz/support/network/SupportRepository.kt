package com.techeaz.support.network

import com.google.gson.GsonBuilder
import com.techeaz.support.data.SupportContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Repository class for handling support content data operations
 */
class SupportRepository {
    
    private val apiService: SupportApiService
    
    init {
        // Configure OkHttp client
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        // Configure Gson
        val gson = GsonBuilder()
            .setLenient()
            .create()
        
        // Configure Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        
        apiService = retrofit.create(SupportApiService::class.java)
    }
    
    /**
     * Fetch support content from the specified GitHub URL
     */
    suspend fun getSupportContent(githubUrl: String): Result<SupportContent> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSupportContent(githubUrl)
                if (response.isSuccessful) {
                    response.body()?.let { supportContent ->
                        Result.success(supportContent)
                    } ?: Result.failure(Exception("Empty response body"))
                } else {
                    Result.failure(Exception("HTTP Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Fetch support content using default GitHub configuration
     * You can modify these values or make them configurable through app settings
     */
    suspend fun getSupportContent(): Result<SupportContent> {
        // Default configuration - you can change these values
        val username = "adsch"  // Replace with your GitHub username
        val repository = "techeaz-support"  // Replace with your repository name
        val branch = "main"
        val filePath = "content.json"
        
        val githubUrl = SupportApiService.buildGitHubUrl(username, repository, branch, filePath)
        return getSupportContent(githubUrl)
    }
    
    /**
     * Get a sample/fallback support content for testing or offline use
     */
    fun getFallbackContent(): SupportContent {
        return SupportContent(
            app = com.techeaz.support.data.AppInfo(
                name = "Techeaz Support",
                description = "Get help and find answers to frequently asked questions",
                version = "1.0.0"
            ),
            contact = com.techeaz.support.data.ContactInfo(
                email = "support@techeaz.com",
                phone = "+1-555-TECHEAZ",
                hours = "Monday - Friday: 9:00 AM - 6:00 PM EST",
                responseTime = "We typically respond within 24 hours"
            ),
            categories = listOf(
                com.techeaz.support.data.Category(
                    id = "general",
                    name = "General Questions",
                    description = "Common questions about our services",
                    icon = "❓"
                ),
                com.techeaz.support.data.Category(
                    id = "technical",
                    name = "Technical Support",
                    description = "Technical issues and troubleshooting",
                    icon = "🔧"
                )
            ),
            faqs = listOf(
                com.techeaz.support.data.FAQ(
                    id = 1,
                    category = "general",
                    question = "How do I get started?",
                    answer = "Welcome! Getting started is easy. Simply follow our quick setup guide and you'll be up and running in minutes.",
                    tags = listOf("setup", "getting-started", "beginner"),
                    helpful = 0,
                    lastUpdated = "2024-01-15"
                ),
                com.techeaz.support.data.FAQ(
                    id = 2,
                    category = "technical",
                    question = "How do I contact support?",
                    answer = "You can contact our support team via email or phone using the contact information provided in the app.",
                    tags = listOf("contact", "support", "help"),
                    helpful = 0,
                    lastUpdated = "2024-01-15"
                )
            ),
            announcements = listOf(
                com.techeaz.support.data.Announcement(
                    id = 1,
                    type = "info",
                    title = "Welcome to Techeaz Support",
                    message = "Thank you for using our support app. We're here to help!",
                    date = "2024-01-15",
                    active = true
                )
            ),
            quickActions = listOf(
                com.techeaz.support.data.QuickAction(
                    title = "Contact Support",
                    description = "Get in touch with our support team",
                    action = "mailto:support@techeaz.com",
                    icon = "📧"
                ),
                com.techeaz.support.data.QuickAction(
                    title = "Call Us",
                    description = "Speak with a support representative",
                    action = "tel:+1-555-TECHEAZ",
                    icon = "📞"
                )
            )
        )
    }
}