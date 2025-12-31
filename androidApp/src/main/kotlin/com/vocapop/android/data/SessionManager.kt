package com.vocapop.android.data

import com.vocapop.data.remote.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Session manager to hold current user info and auth token.
 * In a production app, this would be persisted to secure storage (EncryptedSharedPreferences).
 */
object SessionManager {
    private val _currentUserEmail = MutableStateFlow<String?>(null)
    val currentUserEmail: StateFlow<String?> = _currentUserEmail.asStateFlow()
    
    private val _currentUserName = MutableStateFlow<String?>(null)
    val currentUserName: StateFlow<String?> = _currentUserName.asStateFlow()
    
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()
    
    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    private val _selectedLanguage = MutableStateFlow("fr")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()
    
    private val _dailyGoal = MutableStateFlow(10)
    val dailyGoal: StateFlow<Int> = _dailyGoal.asStateFlow()
    
    /**
     * Login with full user data from API response
     */
    fun loginWithUser(token: String, user: UserDto) {
        _authToken.value = token
        _currentUserId.value = user.id
        _currentUserEmail.value = user.email
        _currentUserName.value = user.getDisplayName()
        _selectedLanguage.value = user.selectedLanguage ?: "fr"
        _dailyGoal.value = user.dailyGoal
        _isLoggedIn.value = true
    }
    
    /**
     * Simple login without API (for demo or local name)
     */
    fun login(email: String, name: String? = null) {
        _currentUserEmail.value = email
        _currentUserName.value = name ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
        _isLoggedIn.value = true
    }
    
    fun logout() {
        _authToken.value = null
        _currentUserId.value = null
        _currentUserEmail.value = null
        _currentUserName.value = null
        _isLoggedIn.value = false
    }
    
    fun isDemoMode(): Boolean = _authToken.value == null && _isLoggedIn.value
    
    fun setDemoMode() {
        _authToken.value = null
        _currentUserEmail.value = "demo@vocapop.com"
        _currentUserName.value = "Learner"
        _isLoggedIn.value = true
    }
    
    fun getToken(): String? = _authToken.value
    
    fun hasValidToken(): Boolean = !_authToken.value.isNullOrBlank()
    
    fun getDisplayName(): String {
        return _currentUserName.value ?: "Learner"
    }
    
    fun getInitial(): String {
        return (_currentUserName.value?.firstOrNull() ?: 'L').uppercase()
    }
    
    fun setLanguage(language: String) {
        _selectedLanguage.value = language
    }
    
    fun setDailyGoal(goal: Int) {
        _dailyGoal.value = goal
    }
}
