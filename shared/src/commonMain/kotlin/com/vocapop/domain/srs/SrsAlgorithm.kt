package com.vocapop.domain.srs

import com.vocapop.domain.models.UserCard
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

enum class ReviewGrade(val value: Int) {
    AGAIN(0),   // Complete failure
    HARD(3),    // Correct with difficulty
    GOOD(4),    // Correct with hesitation
    EASY(5)     // Perfect recall
}

object SrsAlgorithm {
    
    // Standard SM-2 constants
    private const val MIN_EASE_FACTOR = 1.3f
    private const val INITIAL_EASE_FACTOR = 2.5f

    /**
     * Calculate next review parameters based on SM-2 algorithm
     */
    fun calculateNextReview(
        currentCard: UserCard,
        grade: ReviewGrade
    ): UserCard {
        val newEaseFactor = calculateEaseFactor(currentCard.easeFactor, grade)
        val newRepetitions = if (grade.value >= 3) currentCard.repetitions + 1 else 0
        val newInterval = calculateInterval(
            currentCard.intervalDays,
            newRepetitions,
            newEaseFactor,
            grade
        )
        
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val today = now.date
        
        // Calculate next review date
        // If interval is 1 day or more, add to today
        // For 'AGAIN', we might want it sooner (e.g., minutes), but for simplified persistent model, 
        // we'll stick to day-granularity or treat 0 as "same day/minute queue" in a real app.
        // Here we stick to the daily granularity as requested for simplicity in domain model.
        val nextDate = today.plus(DatePeriod(days = newInterval))
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        return currentCard.copy(
            easeFactor = newEaseFactor,
            intervalDays = newInterval,
            repetitions = newRepetitions,
            nextReviewDate = nextDate,
            lastReviewDate = now
        )
    }
    
    fun createInitialUserCard(userId: String, cardId: String): UserCard {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return UserCard(
            id = "", // UUID generation should happen elsewhere or require passing it in
            userId = userId,
            cardId = cardId,
            easeFactor = INITIAL_EASE_FACTOR,
            intervalDays = 0, // Due immediately
            repetitions = 0,
            nextReviewDate = now,
            lastReviewDate = null
        )
    }
    
    private fun calculateEaseFactor(currentEf: Float, grade: ReviewGrade): Float {
        // EF' = EF + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
        val q = grade.value
        val newEf = currentEf + (0.1f - (5 - q) * (0.08f + (5 - q) * 0.02f))
        return maxOf(MIN_EASE_FACTOR, newEf)
    }
    
    private fun calculateInterval(
        currentInterval: Int,
        repetitions: Int,
        easeFactor: Float,
        grade: ReviewGrade
    ): Int {
        return when {
            grade == ReviewGrade.AGAIN -> 1 // Reset to 1 day
            repetitions == 1 -> 1
            repetitions == 2 -> 6
            else -> (currentInterval * easeFactor).toInt() // I(n) = I(n-1) * EF
        }
    }
    
    private fun maxOf(a: Float, b: Float): Float {
        return if (a > b) a else b
    }
}
