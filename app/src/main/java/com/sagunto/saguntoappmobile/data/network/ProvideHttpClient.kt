package com.sagunto.saguntoappmobile.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.sagunto.saguntoappmobile.BuildConfig
import com.sagunto.saguntoappmobile.data.interfaces.IAuthRepository

fun provideHttpClient(authRepository: IAuthRepository): HttpClient {

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

        install(Auth) {
            bearer {
                loadTokens {
                    val token = authRepository.getJwtToken(refresh = false)
                    if (token != null) {
                        BearerTokens(accessToken = token, refreshToken = "")
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val freshToken = authRepository.getJwtToken(refresh = true)
                    if (freshToken != null) {
                        BearerTokens(accessToken = freshToken, refreshToken = "")
                    } else {
                        null
                    }
                }
            }
        }

        defaultRequest {
            url(BuildConfig.API_BASE_URL)
            contentType(ContentType.Application.Json)
        }
    }
}