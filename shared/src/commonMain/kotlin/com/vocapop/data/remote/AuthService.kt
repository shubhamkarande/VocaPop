package com.vocapop.data.remote

import com.vocapop.domain.models.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String, 
    @kotlinx.serialization.SerialName("password_hash")
    val passwordHash: String
)

@Serializable
data class RegisterRequest(
    val name: String?,
    val email: String, 
    @kotlinx.serialization.SerialName("password_hash")
    val passwordHash: String, 
    val language: String?, 
    @kotlinx.serialization.SerialName("daily_goal")
    val dailyGoal: Int
)

@Serializable
data class AuthResponse(
    val token: String, 
    val refreshToken: String, 
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: String,
    val name: String? = null,
    val email: String, 
    val selectedLanguage: String?, 
    val dailyGoal: Int,
    val streak: Int = 0,
    val wordsMastered: Int = 0
) {
    fun toDomain(): User = User(id, email, selectedLanguage, dailyGoal, streak, wordsMastered)
    
    fun getDisplayName(): String {
        return name ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
    }
}

class AuthService(private val client: HttpClient) {
    suspend fun login(email: String, passwordHash: String): AuthResponse {
        return client.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, passwordHash))
        }.body()
    }

    suspend fun register(name: String?, email: String, passwordHash: String, language: String, dailyGoal: Int): AuthResponse {
        return client.post("auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(name, email, passwordHash, language, dailyGoal))
        }.body()
    }
}
