package com.techeaz.support.network

import com.techeaz.support.data.SupportContent
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Retrofit API service interface for fetching support content from GitHub
 */
interface SupportApiService {
    
    /**
     * Fetch support content from GitHub raw URL
     * Example URL: https://raw.githubusercontent.com/username/repo/main/content.json
     */
    @GET
    suspend fun getSupportContent(@Url url: String): Response<SupportContent>
    
    companion object {
        // Default GitHub raw URL pattern
        // You can customize this URL in the app settings
        const val DEFAULT_BASE_URL = "https://raw.githubusercontent.com/"
        const val DEFAULT_CONTENT_PATH = "main/content.json"
        
        /**
         * Build the complete GitHub raw URL
         * @param username GitHub username
         * @param repository Repository name
         * @param branch Branch name (default: main)
         * @param filePath File path (default: content.json)
         */
        fun buildGitHubUrl(
            username: String,
            repository: String,
            branch: String = "main",
            filePath: String = "content.json"
        ): String {
            return "${DEFAULT_BASE_URL}$username/$repository/$branch/$filePath"
        }
    }
}