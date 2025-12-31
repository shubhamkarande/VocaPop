package com.vocapop.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val id: String,
    val deckId: String,
    val front: String,
    val back: String,
    val exampleSentence: String? = null,
    val wordType: String? = null, // Noun, Verb, etc.
    val imageUrl: String? = null,
    val audioUrl: String? = null
)
