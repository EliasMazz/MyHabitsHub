package com.yolo.core.domain.auth

/**
 * Static SSO configuration resolved at startup from build config and provided via DI.
 *
 * [googleWebServerClientId] is the OAuth 2.0 **Web** client ID (client_type 3 in
 * google-services.json) — the value AndroidX Credential Manager requires as its `serverClientId`.
 * Never the Android client ID (that mismatch is the #1 cause of Google sign-in DEVELOPER_ERROR 10).
 */
data class SsoConfig(
    val googleWebServerClientId: String,
)
