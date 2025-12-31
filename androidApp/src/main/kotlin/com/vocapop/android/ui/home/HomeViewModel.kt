package com.vocapop.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocapop.android.data.SessionManager
import com.vocapop.data.remote.DeckDto
import com.vocapop.data.remote.LearningService
import com.vocapop.data.remote.createHttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val decks: List<DeckDto> = emptyList(),
    val userName: String = "Learner",
    val streak: Int = 0,
    val dailyProgress: Int = 0,
    val dailyGoal: Int = 10,
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    
    private val learningService = LearningService(createHttpClient())
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadHomeData()
    }
    
    fun loadHomeData() {
        viewModelScope.launch {
            val token = SessionManager.getToken()
            val userName = SessionManager.getDisplayName()
            val dailyGoal = SessionManager.dailyGoal.value
            
            _uiState.value = _uiState.value.copy(
                userName = userName,
                dailyGoal = dailyGoal
            )
            
            if (token != null && SessionManager.hasValidToken()) {
                // Fetch from real API
                loadFromApi(token)
            } else {
                // Demo mode - use mock data
                loadMockData()
            }
        }
    }
    
    private suspend fun loadFromApi(token: String) {
        try {
            val decks = learningService.getDecks(token)
            
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                decks = decks,
                streak = 5, // TODO: Fetch from user stats endpoint
                dailyProgress = 7
            )
        } catch (e: Exception) {
            // API failed, fall back to mock data
            loadMockData()
            _uiState.value = _uiState.value.copy(
                error = "Could not fetch decks: ${e.message}"
            )
        }
    }
    
    private fun loadMockData() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            decks = listOf(
                DeckDto(
                    id = "1",
                    name = "French Starter",
                    language = "fr",
                    description = "Basic French vocabulary",
                    isStarter = true,
                    cardCount = 30,
                    learnedCount = 15
                ),
                DeckDto(
                    id = "2",
                    name = "Spanish Starter",
                    language = "es",
                    description = "Basic Spanish vocabulary",
                    isStarter = true,
                    cardCount = 25,
                    learnedCount = 10
                ),
                DeckDto(
                    id = "3",
                    name = "Japanese Starter",
                    language = "ja",
                    description = "Basic Japanese vocabulary",
                    isStarter = true,
                    cardCount = 20,
                    learnedCount = 5
                )
            ),
            streak = 5,
            dailyProgress = 7
        )
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
