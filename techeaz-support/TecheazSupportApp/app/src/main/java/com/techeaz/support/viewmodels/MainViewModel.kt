package com.techeaz.support.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.techeaz.support.data.SupportContent
import com.techeaz.support.network.SupportRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for MainActivity - handles loading and managing support content data
 */
class MainViewModel(private val repository: SupportRepository) : ViewModel() {
    
    private val _supportContent = MutableLiveData<SupportContent?>()
    val supportContent: LiveData<SupportContent?> = _supportContent
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing
    
    /**
     * Load support content from GitHub
     */
    fun loadSupportContent() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val result = repository.getSupportContent()
                
                result.onSuccess { content ->
                    _supportContent.value = content
                }.onFailure { exception ->
                    handleError(exception)
                    // Use fallback content if network request fails
                    _supportContent.value = repository.getFallbackContent()
                }
            } catch (e: Exception) {
                handleError(e)
                // Use fallback content as last resort
                _supportContent.value = repository.getFallbackContent()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Refresh content (pull-to-refresh or manual refresh)
     */
    fun refreshContent() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _errorMessage.value = null
            
            try {
                val result = repository.getSupportContent()
                
                result.onSuccess { content ->
                    _supportContent.value = content
                }.onFailure { exception ->
                    handleError(exception)
                }
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _isRefreshing.value = false
            }
        }
    }
    
    /**
     * Load content from a custom GitHub URL
     */
    fun loadContentFromUrl(githubUrl: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val result = repository.getSupportContent(githubUrl)
                
                result.onSuccess { content ->
                    _supportContent.value = content
                }.onFailure { exception ->
                    handleError(exception)
                }
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Clear any error messages
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * Handle errors and convert them to user-friendly messages
     */
    private fun handleError(exception: Throwable) {
        val message = when {
            exception.message?.contains("UnknownHostException") == true -> 
                "No internet connection. Please check your network and try again."
            exception.message?.contains("SocketTimeoutException") == true -> 
                "Request timed out. Please try again."
            exception.message?.contains("HTTP Error: 404") == true -> 
                "Content not found. Please check the GitHub repository URL."
            exception.message?.contains("HTTP Error: 403") == true -> 
                "Access denied. Please check if the repository is public."
            exception.message?.contains("HTTP Error") == true -> 
                "Server error. Please try again later."
            exception.message?.contains("JSON") == true -> 
                "Invalid content format. Please check the JSON file."
            else -> "Error loading content: ${exception.message ?: "Unknown error"}"
        }
        _errorMessage.value = message
    }
    
    /**
     * Get the current support content (synchronous access)
     */
    fun getCurrentContent(): SupportContent? {
        return _supportContent.value
    }
    
    /**
     * Check if content is currently loading
     */
    fun isContentLoading(): Boolean {
        return _isLoading.value == true
    }
    
    /**
     * Factory class for creating MainViewModel instances
     */
    class Factory(private val repository: SupportRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}