package com.vocapop.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Base URL for the API.
 * 
 * For LOCAL DEVELOPMENT:
 * - Android Emulator: Use "http://10.0.2.2:8000/api/"
 * - Physical Device (same network): Use "http://YOUR_LAPTOP_IP:8000/api/"
 *   Run `ipconfig` on Windows to find your local IP (e.g., 192.168.x.x)
 * 
 * For PRODUCTION: Use your deployed server URL
 */
object ApiConfig {
    // Change this to your laptop's IP when testing on physical device
    // Example: "http://192.168.1.5:8000/api/"
    // Physical device via hotspot - use laptop's IP on the hotspot network
    const val BASE_URL = "http://10.0.2.2:8000/api/"
    
    // For production deployment
    // const val BASE_URL = "https://vocapop-api.azurewebsites.net/api/"
}

fun createHttpClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        
        install(Logging) {
            level = LogLevel.ALL
        }
        
        defaultRequest {
            url(ApiConfig.BASE_URL)
            accept(ContentType.Application.Json)
        }
    }
}

