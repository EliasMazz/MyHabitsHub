package com.yolo.auth.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AuthGraphRoutes {
    @Serializable
    data object Graph: AuthGraphRoutes

    @Serializable
    data object Welcome: AuthGraphRoutes

    @Serializable
    data class Login(val email: String = ""): AuthGraphRoutes

    @Serializable
    data class Register(val email: String = ""): AuthGraphRoutes

    @Serializable
    data class RegisterSuccess(val email: String): AuthGraphRoutes

    @Serializable
    data class ForgotPassword(val email: String = ""): AuthGraphRoutes

    @Serializable
    data class ResetPassword(val token: String = ""): AuthGraphRoutes

    @Serializable
    data class EmailVerification(val token: String = ""): AuthGraphRoutes
}