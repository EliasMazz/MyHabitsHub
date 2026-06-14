package com.yolo.auth.presentation.sso

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.AuthenticationServices.ASAuthorization
import platform.AuthenticationServices.ASAuthorizationAppleIDCredential
import platform.AuthenticationServices.ASAuthorizationAppleIDProvider
import platform.AuthenticationServices.ASAuthorizationController
import platform.AuthenticationServices.ASAuthorizationControllerDelegateProtocol
import platform.AuthenticationServices.ASAuthorizationControllerPresentationContextProvidingProtocol
import platform.AuthenticationServices.ASAuthorizationScopeEmail
import platform.AuthenticationServices.ASAuthorizationScopeFullName
import platform.AuthenticationServices.ASPresentationAnchor
import platform.CoreCrypto.CC_SHA256
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Security.SecRandomCopyBytes
import platform.Security.kSecRandomDefault
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.darwin.NSObject

/** Google sign-in is never shown on iOS (we use Apple there). */
@Composable
actual fun rememberGoogleSignIn(
    serverClientId: String,
    onResult: (SocialSignInResult) -> Unit,
): () -> Unit = remember { {} }

/**
 * Apple Sign-In via the native AuthenticationServices framework (ASAuthorization), implemented
 * entirely in Kotlin/Native — no Swift, no Firebase. Returns the raw `identityToken` +
 * `authorizationCode` + the un-hashed nonce for the backend to verify and exchange.
 */
@Composable
actual fun rememberAppleSignIn(
    onResult: (SocialSignInResult) -> Unit,
): () -> Unit {
    val currentOnResult by rememberUpdatedState(onResult)
    // Retain the controller across the async flow — ASAuthorizationController.delegate is weak.
    val holder = remember { AppleSignInHolder() }
    return remember { { holder.start { result -> currentOnResult(result) } } }
}

private class AppleSignInHolder {
    private var controller: AppleSignInController? = null

    fun start(onResult: (SocialSignInResult) -> Unit) {
        if (controller != null) return // a request is already in flight — ignore re-taps
        val signInController = AppleSignInController { result ->
            controller = null
            onResult(result)
        }
        controller = signInController
        signInController.start()
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class AppleSignInController(
    private val onResult: (SocialSignInResult) -> Unit,
) : NSObject(),
    ASAuthorizationControllerDelegateProtocol,
    ASAuthorizationControllerPresentationContextProvidingProtocol {

    private var rawNonce: String = ""

    // The controller's delegate/presentationContextProvider are weak; keep a strong reference so the
    // controller (and this delegate, retained by the caller's holder) survives until the callback.
    private var authorizationController: ASAuthorizationController? = null

    fun start() {
        rawNonce = randomNonce()
        val request = ASAuthorizationAppleIDProvider().createRequest().apply {
            requestedScopes = listOf(ASAuthorizationScopeFullName, ASAuthorizationScopeEmail)
            // Apple receives the SHA-256 hash; the backend receives the raw nonce and re-hashes.
            nonce = sha256Hex(rawNonce)
        }
        val controller = ASAuthorizationController(authorizationRequests = listOf(request))
        controller.delegate = this
        controller.presentationContextProvider = this
        authorizationController = controller
        controller.performRequests()
    }

    override fun authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithAuthorization: ASAuthorization,
    ) {
        val credential = didCompleteWithAuthorization.credential as? ASAuthorizationAppleIDCredential
        if (credential == null) {
            onResult(SocialSignInResult.Failure("Unexpected Apple credential type"))
            return
        }
        val identityToken = credential.identityToken?.toUtf8String().orEmpty()
        val authorizationCode = credential.authorizationCode?.toUtf8String().orEmpty()
        if (identityToken.isBlank()) {
            onResult(SocialSignInResult.Failure("Missing Apple identity token"))
            return
        }
        onResult(
            SocialSignInResult.AppleSuccess(
                identityToken = identityToken,
                authorizationCode = authorizationCode,
                nonce = rawNonce,
            )
        )
    }

    override fun authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithError: NSError,
    ) {
        // ASAuthorizationError.canceled == 1001
        if (didCompleteWithError.code == 1001L) {
            onResult(SocialSignInResult.Cancelled)
        } else {
            onResult(SocialSignInResult.Failure(didCompleteWithError.localizedDescription))
        }
    }

    override fun presentationAnchorForAuthorizationController(
        controller: ASAuthorizationController,
    ): ASPresentationAnchor = currentKeyWindow()
}

@OptIn(BetaInteropApi::class)
private fun NSData.toUtf8String(): String? =
    NSString.create(data = this, encoding = NSUTF8StringEncoding)?.toString()

@OptIn(ExperimentalForeignApi::class)
private fun randomNonce(length: Int = 32): String {
    // Exactly 64 chars (base64url alphabet) so `byte % 64` is bias-free (256 % 64 == 0).
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
    val randomBytes = ByteArray(length)
    randomBytes.usePinned { pinned ->
        SecRandomCopyBytes(kSecRandomDefault, length.convert(), pinned.addressOf(0))
    }
    return randomBytes.joinToString("") { byte ->
        charset[(byte.toInt() and 0xFF) % charset.length].toString()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun sha256Hex(input: String): String {
    val inputBytes = input.encodeToByteArray()
    val digest = UByteArray(CC_SHA256_DIGEST_LENGTH)
    inputBytes.usePinned { inputPinned ->
        digest.usePinned { digestPinned ->
            CC_SHA256(
                if (inputBytes.isNotEmpty()) inputPinned.addressOf(0) else null,
                inputBytes.size.convert(),
                digestPinned.addressOf(0),
            )
        }
    }
    return digest.joinToString("") { byte -> byte.toInt().toString(16).padStart(2, '0') }
}

private fun currentKeyWindow(): ASPresentationAnchor {
    val windows = UIApplication.sharedApplication.windows
    val keyWindow = windows.firstOrNull { (it as? UIWindow)?.keyWindow == true } as? UIWindow
    return keyWindow ?: (windows.firstOrNull() as? UIWindow) ?: UIWindow()
}
