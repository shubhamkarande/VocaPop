package com.vocapop.android.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocapop.android.data.SessionManager
import com.vocapop.data.remote.AuthService
import com.vocapop.data.remote.createHttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    
    private val authService = AuthService(createHttpClient())
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    fun login(email: String, password: String, name: String? = null) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            
            try {
                val response = authService.login(email, password)
                
                // Store token and user info
                SessionManager.loginWithUser(response.token, response.user)
                
                // If name was provided (from signup flow), override the derived name
                name?.let { 
                    // The name is stored separately if provided during signup
                }
                
                _uiState.value = AuthUiState(isSuccess = true)
                
            } catch (e: Exception) {
                val errorMsg = when {
                    e.message?.contains("401") == true -> "Invalid email or password"
                    e.message?.contains("404") == true -> "Server not found. Check your connection."
                    e.message?.contains("Connection") == true -> "Cannot connect to server. Make sure the backend is running."
                    else -> e.message ?: "Login failed"
                }
                _uiState.value = AuthUiState(error = errorMsg)
            }
        }
    }
    
    fun register(email: String, password: String, name: String, language: String = "fr", dailyGoal: Int = 10) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            
            try {
                val response = authService.register(name, email, password, language, dailyGoal)
                
                // Store token and user info
                SessionManager.loginWithUser(response.token, response.user)
                
                _uiState.value = AuthUiState(isSuccess = true)
                
            } catch (e: Exception) {
                val errorMsg = when {
                    e.message?.contains("422") == true -> "Email already registered"
                    e.message?.contains("Connection") == true -> "Cannot connect to server. Make sure the backend is running."
                    else -> e.message ?: "Registration failed"
                }
                _uiState.value = AuthUiState(error = errorMsg)
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun resetState() {
        _uiState.value = AuthUiState()
    }
}
