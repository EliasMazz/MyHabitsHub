package com.yolo.core.data.networking

import com.yolo.core.data.auth.dto.request.RefreshTokenRequest
import com.yolo.core.data.auth.dto.response.AuthInfoResponse
import com.yolo.core.data.logging.AppLogger
import com.yolo.core.data.mapper.toDomain
import com.yolo.core.domain.auth.SessionStorage
import com.yolo.core.domain.util.onFailure
import com.yolo.core.domain.util.onSuccess
import com.yolo.myhabitshub.core.data.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val sessionStorage: SessionStorage
) {

    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        AppLogger.d("NetworkRequest: $message")
                    }
                }
                level = LogLevel.ALL
            }
            install(WebSockets) {
                pingIntervalMillis = 20_000L
            }
            defaultRequest {
                header("x-api-key", BuildKonfig.API_KEY)
                contentType(ContentType.Application.Json)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        sessionStorage.observeAuthInfo()
                            .firstOrNull()?.let {
                                BearerTokens(it.accessToken, it.refreshToken)
                            }
                    }
                    refreshTokens {
                        if (response.request.url.encodedPath.contains("/api/auth")) {
                            return@refreshTokens null
                        }

                        val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
                        if (authInfo?.refreshToken.isNullOrBlank()) {
                            sessionStorage.set(null)
                            return@refreshTokens null
                        }

                        var bearerTokens: BearerTokens? = null

                        client.post<RefreshTokenRequest, AuthInfoResponse>(
                            route = "api/auth/refresh",
                            body = RefreshTokenRequest(authInfo.refreshToken),
                            builder = { markAsRefreshTokenRequest() }
                        ).onSuccess { newAuthInfo ->
                            sessionStorage.set(newAuthInfo.toDomain())
                            bearerTokens = BearerTokens(
                                newAuthInfo.accessToken,
                                newAuthInfo.refreshToken
                            )
                        }.onFailure {
                            //If the short live access token and long live refresh token expires
                            sessionStorage.set(null)
                            return@refreshTokens null
                        }

                        bearerTokens
                    }
                }
            }
        }
    }
}