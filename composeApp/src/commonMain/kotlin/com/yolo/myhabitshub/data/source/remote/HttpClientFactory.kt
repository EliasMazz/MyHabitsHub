package com.yolo.myhabitshub.data.source.remote

import com.yolo.myhabitshub.util.logging.AppLogger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object HttpClientFactory {
    fun default() = HttpClient {
        defaultRequest {
            //TEST ONLY NEED TO CHANGE TO BASE URL AFTER SERVER DEPLOY
            url("http://10.0.2.2:8080")
            header(HttpHeaders.ContentType, "application/json")
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60000  // Total request timeout: 60 seconds
            connectTimeoutMillis = 10000 // Connection establishment timeout: 10 seconds
            socketTimeoutMillis = 60000  // Inactivity timeout: 60 seconds
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
            exponentialDelay()
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    AppLogger.d("NetworkRequest: $message")
                }
            }
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                explicitNulls = false
            })
        }
    }.also {
        it.plugin(HttpSend).intercept { request ->
            // TODO Improve force refresh only on 401 status
            val userToken = Firebase.auth.currentUser?.getIdToken(true)
            request.header("Authorization", "Bearer $userToken")
            execute(request)
        }
    }


}

