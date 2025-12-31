package com.vocapop.domain.repository

import com.vocapop.domain.models.CardWithProgress
import com.vocapop.domain.models.Deck
import com.vocapop.domain.models.SyncResult
import com.vocapop.domain.models.UserCard
import com.vocapop.domain.srs.ReviewGrade
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    suspend fun getDecks(): Flow<List<Deck>>
    suspend fun getDeck(id: String): Deck?
    suspend fun getDueCards(deckId: String? = null): Flow<List<CardWithProgress>>
    suspend fun submitReview(cardId: String, grade: ReviewGrade): Result<UserCard>
    suspend fun syncWithServer(): Result<SyncResult>
    suspend fun createDeck(name: String, language: String, description: String?): Result<Deck>
    suspend fun importStarterDeck(deckId: String): Result<Unit>
}
