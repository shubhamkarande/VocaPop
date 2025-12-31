package com.vocapop.domain.repository

import com.vocapop.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, language: String, dailyGoal: Int): Result<User>
    suspend fun getCurrentUser(): Flow<User?>
    suspend fun updateDailyGoal(newGoal: Int): Result<User>
    suspend fun updateSelectedLanguage(language: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun isAuthenticated(): Boolean
}
