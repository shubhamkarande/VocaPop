package com.vocapop.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeckDto(
    val id: String,
    val name: String,
    val language: String,
    val description: String? = null,
    @SerialName("is_starter")
    val isStarter: Boolean = false,
    @SerialName("card_count")
    val cardCount: Int = 0,
    @SerialName("learned_count")
    val learnedCount: Int = 0
)

@Serializable
data class CardDto(
    val id: String,
    @SerialName("deck_id")
    val deckId: String,
    val front: String,
    val back: String,
    @SerialName("example_sentence")
    val exampleSentence: String? = null,
    @SerialName("word_type")
    val wordType: String? = null,
    @SerialName("audio_url")
    val audioUrl: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null
)

@Serializable
data class UserCardDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("card_id")
    val cardId: String,
    @SerialName("ease_factor")
    val easeFactor: Float = 2.5f,
    @SerialName("interval_days")
    val intervalDays: Int = 1,
    val repetitions: Int = 0,
    @SerialName("next_review_date")
    val nextReviewDate: String? = null,
    @SerialName("last_review_date")
    val lastReviewDate: String? = null
)

@Serializable
data class CardWithProgressDto(
    val card: CardDto,
    val progress: UserCardDto
)

@Serializable
data class ReviewRequest(
    @SerialName("card_id")
    val cardId: String,
    val grade: Int
)

class LearningService(private val client: HttpClient) {
    
    suspend fun getDecks(token: String): List<DeckDto> {
        return client.get("decks") {
            header("Authorization", "Bearer $token")
        }.body()
    }
    
    suspend fun getDueCards(token: String): List<CardWithProgressDto> {
        return client.get("cards/due") {
            header("Authorization", "Bearer $token")
        }.body()
    }
    
    suspend fun submitReview(token: String, cardId: String, grade: Int): UserCardDto {
        return client.post("review") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(ReviewRequest(cardId, grade))
        }.body()
    }
}
