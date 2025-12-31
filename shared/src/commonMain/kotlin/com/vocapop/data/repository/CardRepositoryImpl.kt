package com.vocapop.data.repository

import com.vocapop.data.local.DatabaseHelper
import com.vocapop.domain.models.*
import com.vocapop.domain.repository.CardRepository
import com.vocapop.domain.srs.ReviewGrade
import com.vocapop.domain.srs.SrsAlgorithm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class CardRepositoryImpl(
    private val dbHelper: DatabaseHelper
) : CardRepository {

    override suspend fun getDecks(): Flow<List<Deck>> = flow {
        val entities = withContext(Dispatchers.Default) {
            dbHelper.queries.getAllDecks().executeAsList()
        }
        emit(entities.map { entity ->
            Deck(
                id = entity.id,
                name = entity.name,
                language = entity.language,
                description = entity.description,
                isStarter = entity.isStarter != 0L,
                cardCount = entity.cardCount.toInt(),
                learnedCount = entity.learnedCount.toInt()
            )
        })
    }

    override suspend fun getDeck(id: String): Deck? {
        val entity = withContext(Dispatchers.Default) {
            dbHelper.queries.getDeckById(id).executeAsOneOrNull()
        }
        return entity?.let {
            Deck(
                id = it.id,
                name = it.name,
                language = it.language,
                description = it.description,
                isStarter = it.isStarter != 0L,
                cardCount = it.cardCount.toInt(),
                learnedCount = it.learnedCount.toInt()
            )
        }
    }

    override suspend fun getDueCards(deckId: String?): Flow<List<CardWithProgress>> = flow {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        
        val results = withContext(Dispatchers.Default) {
            dbHelper.queries.getDueUserCards(now).executeAsList()
        }
        
        emit(results.map { row ->
            CardWithProgress(
                card = Card(
                    id = row.id_,
                    deckId = row.deckId,
                    front = row.front,
                    back = row.back,
                    exampleSentence = row.exampleSentence,
                    wordType = row.wordType,
                    imageUrl = row.imageUrl,
                    audioUrl = row.audioUrl
                ),
                progress = UserCard(
                    id = row.id,
                    userId = row.userId,
                    cardId = row.cardId,
                    easeFactor = row.easeFactor.toFloat(),
                    intervalDays = row.intervalDays.toInt(),
                    repetitions = row.repetitions.toInt(),
                    nextReviewDate = kotlinx.datetime.LocalDateTime.parse(row.nextReviewDate),
                    lastReviewDate = row.lastReviewDate?.let { kotlinx.datetime.LocalDateTime.parse(it) }
                )
            )
        })
    }

    override suspend fun submitReview(cardId: String, grade: ReviewGrade): Result<UserCard> {
        return try {
            Result.success(SrsAlgorithm.createInitialUserCard("user", cardId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncWithServer(): Result<SyncResult> {
        return Result.success(SyncResult(true))
    }

    override suspend fun createDeck(name: String, language: String, description: String?): Result<Deck> {
        return Result.failure(Exception("Not implemented"))
    }

    override suspend fun importStarterDeck(deckId: String): Result<Unit> {
        return Result.success(Unit)
    }
}
