package com.vocapop.domain.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserCard(
    val id: String,
    val userId: String,
    val cardId: String,
    val easeFactor: Float = 2.5f,
    val intervalDays: Int = 1,
    val repetitions: Int = 0,
    val nextReviewDate: LocalDateTime,
    val lastReviewDate: LocalDateTime? = null
)

@Serializable
data class CardWithProgress(
    val card: Card,
    val progress: UserCard? = null
)
