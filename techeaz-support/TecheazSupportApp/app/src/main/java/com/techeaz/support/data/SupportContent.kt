package com.techeaz.support.data

import com.google.gson.annotations.SerializedName

/**
 * Main data class representing the entire support content structure
 */
data class SupportContent(
    @SerializedName("app")
    val app: AppInfo,
    
    @SerializedName("contact")
    val contact: ContactInfo,
    
    @SerializedName("categories")
    val categories: List<Category>,
    
    @SerializedName("faqs")
    val faqs: List<FAQ>,
    
    @SerializedName("announcements")
    val announcements: List<Announcement>,
    
    @SerializedName("quick_actions")
    val quickActions: List<QuickAction>
)

/**
 * App information
 */
data class AppInfo(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("version")
    val version: String
)

/**
 * Contact information
 */
data class ContactInfo(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String,
    
    @SerializedName("hours")
    val hours: String,
    
    @SerializedName("response_time")
    val responseTime: String
)

/**
 * FAQ Category
 */
data class Category(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("icon")
    val icon: String
) {
    // Helper method to get category color
    fun getColor(): String {
        return when (id) {
            "general" -> "#8B5CF6"
            "account" -> "#06B6D4"
            "technical" -> "#F59E0B"
            "getting-started" -> "#10B981"
            else -> "#64748B"
        }
    }
}

/**
 * FAQ Item
 */
data class FAQ(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("question")
    val question: String,
    
    @SerializedName("answer")
    val answer: String,
    
    @SerializedName("tags")
    val tags: List<String>,
    
    @SerializedName("helpful")
    val helpful: Int,
    
    @SerializedName("last_updated")
    val lastUpdated: String
) {
    // Helper method to check if FAQ matches search query
    fun matchesSearch(query: String): Boolean {
        val lowerQuery = query.lowercase()
        return question.lowercase().contains(lowerQuery) ||
                answer.lowercase().contains(lowerQuery) ||
                tags.any { it.lowercase().contains(lowerQuery) }
    }
    
    // Helper method to get formatted last updated date
    fun getFormattedLastUpdated(): String {
        return try {
            val parts = lastUpdated.split("-")
            if (parts.size == 3) {
                "${parts[1]}/${parts[2]}/${parts[0]}"
            } else {
                lastUpdated
            }
        } catch (e: Exception) {
            lastUpdated
        }
    }
}

/**
 * Announcement
 */
data class Announcement(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("type")
    val type: String, // "info", "warning", "error"
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("date")
    val date: String,
    
    @SerializedName("active")
    val active: Boolean
) {
    // Helper method to get announcement color based on type
    fun getTypeColor(): String {
        return when (type) {
            "warning" -> "#F59E0B"
            "error" -> "#EF4444"
            "info" -> "#3B82F6"
            else -> "#10B981"
        }
    }
    
    // Helper method to get formatted date
    fun getFormattedDate(): String {
        return try {
            val parts = date.split("-")
            if (parts.size == 3) {
                "${parts[1]}/${parts[2]}/${parts[0]}"
            } else {
                date
            }
        } catch (e: Exception) {
            date
        }
    }
}

/**
 * Quick Action
 */
data class QuickAction(
    @SerializedName("title")
    val title: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("action")
    val action: String, // URL, email, or action identifier
    
    @SerializedName("icon")
    val icon: String
) {
    // Helper method to determine action type
    fun getActionType(): ActionType {
        return when {
            action.startsWith("mailto:") -> ActionType.EMAIL
            action.startsWith("tel:") -> ActionType.PHONE
            action.startsWith("http") -> ActionType.URL
            action.startsWith("#") -> ActionType.INTERNAL
            else -> ActionType.UNKNOWN
        }
    }
    
    enum class ActionType {
        EMAIL, PHONE, URL, INTERNAL, UNKNOWN
    }
}