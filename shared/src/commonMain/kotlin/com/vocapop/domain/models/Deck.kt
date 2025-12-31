package com.vocapop.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Deck(
    val id: String,
    val name: String,
    val language: String, // e.g., "es", "fr"
    val description: String? = null,
    val isStarter: Boolean = false,
    val cardCount: Int = 0,
    val learnedCount: Int = 0
)
