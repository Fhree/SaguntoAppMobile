package com.sagunto.saguntoappmobile.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.sagunto.saguntoappmobile.BuildConfig
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger

fun provideHttpClient(): HttpClient {

    // TRAMPA PARA EL DEBUGGER: Imprimimos la variable pura autogenerada por Gradle
    println("🚨 KTOR BASE URL INYECTADA: ${BuildConfig.API_BASE_URL}")

    return HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }

        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }

        defaultRequest {
            url(BuildConfig.API_BASE_URL)
            contentType(ContentType.Application.Json)
        }
    }
}