package com.yolo.auth.domain

import com.yolo.core.domain.auth.AuthInfo
import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.usecase.UseCase
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.fold

/**
 * Exchanges a native social-sign-in credential (Google ID token on Android, Apple identity token on
 * iOS) for the app's own session via the custom backend. Mirrors [LoginAuthUseCase]: it returns the
 * resulting [AuthInfo]; persisting it to the session is the caller's responsibility.
 */
class SsoLoginUseCase(
    private val authRepository: AuthRepository,
) : UseCase<SsoLoginParam, SsoLoginResult>() {
    override suspend fun execute(param: SsoLoginParam): SsoLoginResult {
        val result = when (param) {
            is SsoLoginParam.Google -> authRepository.loginWithGoogle(param.idToken)
            is SsoLoginParam.Apple -> authRepository.loginWithApple(
                identityToken = param.identityToken,
                authorizationCode = param.authorizationCode,
                nonce = param.nonce,
            )
        }
        return result.fold(
            onSuccess = { authInfo -> SsoLoginResult.Success(authInfo) },
            onFailure = { error -> SsoLoginResult.Error(error) },
        )
    }
}

sealed interface SsoLoginParam {
    data class Google(val idToken: String) : SsoLoginParam
    data class Apple(
        val identityToken: String,
        val authorizationCode: String,
        val nonce: String,
    ) : SsoLoginParam
}

sealed interface SsoLoginResult {
    data class Success(val authInfo: AuthInfo) : SsoLoginResult
    data class Error(val dataError: DataError) : SsoLoginResult
}
