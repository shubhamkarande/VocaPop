package com.vocapop.android.ui.learning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocapop.android.data.SessionManager
import com.vocapop.data.remote.LearningService
import com.vocapop.data.remote.createHttpClient
import com.vocapop.domain.models.Card
import com.vocapop.domain.models.CardWithProgress
import com.vocapop.domain.repository.CardRepository
import com.vocapop.domain.srs.ReviewGrade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SessionState(
    val isLoading: Boolean = true,
    val cards: List<CardWithProgress> = emptyList(),
    val currentCardIndex: Int = 0,
    val isCardFlipped: Boolean = false,
    val isSessionComplete: Boolean = false,
    val sessionStats: SessionStats = SessionStats(),
    val error: String? = null
)

data class SessionStats(
    val cardsReviewed: Int = 0,
    val correctCount: Int = 0, // Good + Easy
    val newLearned: Int = 0
)

class SessionViewModel(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val learningService = LearningService(createHttpClient())
    
    private val _uiState = MutableStateFlow(SessionState())
    val uiState: StateFlow<SessionState> = _uiState.asStateFlow()

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            val token = SessionManager.getToken()
            
            if (token != null && SessionManager.hasValidToken()) {
                // Fetch from real API
                loadFromApi(token)
            } else {
                // Demo mode - use local database or mock data
                loadFromLocalOrMock()
            }
        }
    }
    
    private suspend fun loadFromApi(token: String) {
        try {
            val dueCards = learningService.getDueCards(token)
            
            if (dueCards.isNotEmpty()) {
                val cards = dueCards.map { dto ->
                    CardWithProgress(
                        card = Card(
                            id = dto.card.id,
                            deckId = dto.card.deckId,
                            front = dto.card.front,
                            back = dto.card.back,
                            exampleSentence = dto.card.exampleSentence,
                            wordType = dto.card.wordType,
                            audioUrl = dto.card.audioUrl,
                            imageUrl = dto.card.imageUrl
                        ),
                        progress = null // Progress tracked server-side, not needed locally
                    )
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    cards = cards
                )
            } else {
                // No cards due from API - show mock for demo
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    cards = MockData.getMockCards()
                )
            }
        } catch (e: Exception) {
            // API failed, fall back to mock data
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                cards = MockData.getMockCards(),
                error = "Could not fetch cards: ${e.message}"
            )
        }
    }
    
    private suspend fun loadFromLocalOrMock() {
        cardRepository.getDueCards().collect { dueCards ->
            if (_uiState.value.cards.isEmpty() && !_uiState.value.isSessionComplete) {
                if (dueCards.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        cards = dueCards
                    )
                } else {
                    // No cards in local database - use mock data for demo
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        cards = MockData.getMockCards()
                    )
                }
            }
        }
    }

    fun flipCard() {
        _uiState.value = _uiState.value.copy(isCardFlipped = !_uiState.value.isCardFlipped)
    }

    fun submitGrade(grade: ReviewGrade) {
        val currentState = _uiState.value
        val currentCard = currentState.cards.getOrNull(currentState.currentCardIndex) ?: return

        viewModelScope.launch {
            val token = SessionManager.getToken()
            
            // Submit to API if logged in
            if (token != null && SessionManager.hasValidToken()) {
                try {
                    val gradeValue = when (grade) {
                        ReviewGrade.AGAIN -> 0
                        ReviewGrade.HARD -> 3
                        ReviewGrade.GOOD -> 4
                        ReviewGrade.EASY -> 5
                    }
                    learningService.submitReview(token, currentCard.card.id, gradeValue)
                } catch (e: Exception) {
                    // Continue even if API fails - we can sync later
                }
            }
            
            // Also update local
            cardRepository.submitReview(currentCard.card.id, grade)
            
            // Update stats
            val isCorrect = grade == ReviewGrade.GOOD || grade == ReviewGrade.EASY
            val newStats = currentState.sessionStats.copy(
                cardsReviewed = currentState.sessionStats.cardsReviewed + 1,
                correctCount = if (isCorrect) currentState.sessionStats.correctCount + 1 else currentState.sessionStats.correctCount
            )

            // Move to next card
            val nextIndex = currentState.currentCardIndex + 1
            if (nextIndex >= currentState.cards.size) {
                _uiState.value = currentState.copy(
                    isSessionComplete = true,
                    sessionStats = newStats
                )
            } else {
                _uiState.value = currentState.copy(
                    currentCardIndex = nextIndex,
                    isCardFlipped = false,
                    sessionStats = newStats
                )
            }
        }
    }
}

object MockData {
    fun getMockCards(): List<CardWithProgress> {
        return listOf(
            CardWithProgress(
                card = Card(
                    id = "1", deckId = "1", 
                    front = "Le Chat", back = "The Cat", 
                    exampleSentence = "Le chat dort sur le lit.", wordType = "Noun"
                ),
                progress = null
            ),
             CardWithProgress(
                card = Card(
                    id = "2", deckId = "1", 
                    front = "Manger", back = "To Eat", 
                    exampleSentence = "J'aime manger des pommes.", wordType = "Verb"
                ),
                progress = null
            ),
             CardWithProgress(
                card = Card(
                    id = "3", deckId = "1", 
                    front = "Heureux", back = "Happy", 
                    exampleSentence = "Je suis très heureux aujourd'hui.", wordType = "Adjective"
                ),
                progress = null
            )
        )
    }
}
