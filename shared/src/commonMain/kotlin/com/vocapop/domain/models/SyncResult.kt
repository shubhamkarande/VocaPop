package com.vocapop.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class SyncResult(
    val success: Boolean,
    val syncedCardsCount: Int = 0,
    val syncedDecksCount: Int = 0,
    val timestamp: Long = 0L,
    val errors: List<String> = emptyList()
)
