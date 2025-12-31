package com.vocapop.data.repository

import com.vocapop.data.local.DatabaseHelper
import com.vocapop.data.remote.AuthService
import com.vocapop.domain.models.User
import com.vocapop.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val dbHelper: DatabaseHelper,
    private val authService: AuthService
) : UserRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authService.login(email, password)
            
            dbHelper.queries.insertUser(
                id = response.user.id,
                email = response.user.email,
                selectedLanguage = response.user.selectedLanguage,
                dailyGoal = response.user.dailyGoal.toLong(),
                streak = response.user.streak.toLong(),
                wordsMastered = response.user.wordsMastered.toLong(),
                accessToken = response.token,
                refreshToken = response.refreshToken,
                lastSyncTimestamp = 0
            )
            
            Result.success(response.user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, language: String, dailyGoal: Int): Result<User> {
        return try {
            val response = authService.register(null, email, password, language, dailyGoal)
            
            dbHelper.queries.insertUser(
                id = response.user.id,
                email = response.user.email,
                selectedLanguage = response.user.selectedLanguage,
                dailyGoal = response.user.dailyGoal.toLong(),
                streak = response.user.streak.toLong(),
                wordsMastered = response.user.wordsMastered.toLong(),
                accessToken = response.token,
                refreshToken = response.refreshToken,
                lastSyncTimestamp = 0
            )
            
            Result.success(response.user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Flow<User?> = flow {
        val entity = withContext(Dispatchers.Default) {
            dbHelper.queries.getUser().executeAsOneOrNull()
        }
        emit(entity?.let {
            User(
                id = it.id,
                email = it.email,
                selectedLanguage = it.selectedLanguage,
                dailyGoal = it.dailyGoal.toInt(),
                streak = it.streak.toInt(),
                wordsMastered = it.wordsMastered.toInt()
            )
        })
    }
    
    override suspend fun updateDailyGoal(newGoal: Int): Result<User> {
        return Result.failure(Exception("Not implemented"))
    }

    override suspend fun updateSelectedLanguage(language: String): Result<User> {
        return Result.failure(Exception("Not implemented"))
    }

    override suspend fun logout(): Result<Unit> {
        dbHelper.clearDatabase()
        return Result.success(Unit)
    }

    override suspend fun isAuthenticated(): Boolean {
        val user = dbHelper.queries.getUser().executeAsOneOrNull()
        return user?.accessToken != null
    }
}
