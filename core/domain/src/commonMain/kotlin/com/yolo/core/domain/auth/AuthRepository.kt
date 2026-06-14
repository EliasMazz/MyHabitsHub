package com.yolo.core.domain.auth

import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.EmptyResult
import com.yolo.core.domain.util.ResultData

interface AuthRepository {

    suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Remote>

    suspend fun resendVerificationEmail(
        email: String
    ): EmptyResult<DataError.Remote>

    suspend fun verifyEmail(
        token: String
    ): EmptyResult<DataError.Remote>

    suspend fun forgotPassword(
        email: String
    ): EmptyResult<DataError.Remote>

    suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote>

    suspend fun login(
        email: String,
        password: String
    ) : ResultData<AuthInfo, DataError.Remote>

    /**
     * Exchanges a Google ID token (obtained on Android via Credential Manager) for an app session.
     * The backend verifies the token against Google's public keys and returns its own JWT pair.
     */
    suspend fun loginWithGoogle(
        idToken: String
    ): ResultData<AuthInfo, DataError.Remote>

    /**
     * Exchanges an Apple identity token + authorization code (obtained on iOS via ASAuthorization)
     * for an app session. [nonce] is the RAW nonce; the backend hashes it (SHA-256) and compares it
     * with the `nonce` claim inside the identity token to defend against replay.
     */
    suspend fun loginWithApple(
        identityToken: String,
        authorizationCode: String,
        nonce: String,
    ): ResultData<AuthInfo, DataError.Remote>
}