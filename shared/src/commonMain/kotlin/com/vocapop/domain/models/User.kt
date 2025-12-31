package com.vocapop.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val selectedLanguage: String? = null,
    val dailyGoal: Int = 10,
    val streak: Int = 0,
    val wordsMastered: Int = 0
)
