package com.yolo.auth.presentation.reset_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yolo.auth.domain.ResetPasswordParams
import com.yolo.auth.domain.ResetPasswordResult
import com.yolo.auth.domain.ResetPasswordUseCase
import com.yolo.auth.presentation.navigation.AuthGraphRoutes
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.validator.PasswordValidatorUseCase
import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.util.toUiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_password
import myhabitshub.feature.auth.presentation.generated.resources.error_password_required

class ResetPasswordViewModel(
    savedStateHandle: SavedStateHandle,
    private val passwordValidatorUseCase: PasswordValidatorUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : BaseViewModel<ResetPasswordViewIntent, ResetPasswordViewState, ResetPasswordViewEvent>(
    initialState = ResetPasswordViewState()
) {

    private val token: String = savedStateHandle.toRoute<AuthGraphRoutes.ResetPassword>().token

    init {
        // Blank-token check at entry, not at submit (spec §4.6#2): the user must never type a
        // full password before learning the link is dead. No network call.
        if (token.isBlank()) {
            updateState { copy(isTokenInvalid = true) }
        }
    }

    override fun onViewIntent(intent: ResetPasswordViewIntent) {
        when (intent) {
            is ResetPasswordViewIntent.OnPasswordChange -> handlePasswordChange(intent.password)
            ResetPasswordViewIntent.OnTogglePasswordVisibility -> updateState {
                copy(isPasswordVisible = !isPasswordVisible)
            }

            ResetPasswordViewIntent.OnSubmitClick -> submitNewPassword()
            ResetPasswordViewIntent.OnLogInClick -> sendEvent(ResetPasswordViewEvent.NavigateToLogin)
            ResetPasswordViewIntent.OnRequestNewLinkClick ->
                sendEvent(ResetPasswordViewEvent.NavigateToForgotPassword)
        }
    }

    private fun handlePasswordChange(password: String) {
        // Errors clear at keystroke level (V2/E5).
        updateState {
            copy(
                password = password,
                passwordError = null,
                errorText = null,
            )
        }
    }

    private fun submitNewPassword() {
        // B3 double-submit guard. Button is always enabled (B1/B5) — no canSubmit gating.
        if (state.value.isLoading) return

        viewModelScope.launch {
            val password = state.value.password

            // V4 submit-press validation; password errors live at the field only (E1/§4.6#6).
            if (password.isBlank()) {
                updateState { copy(passwordError = UiText.Resource(Res.string.error_password_required)) }
                return@launch
            }

            if (!passwordValidatorUseCase(password)) {
                updateState { copy(passwordError = UiText.Resource(Res.string.error_invalid_password)) }
                return@launch
            }

            updateState { copy(isLoading = true, passwordError = null, errorText = null) }

            when (val result = resetPasswordUseCase(
                ResetPasswordParams(newPassword = password, token = token)
            )) {
                is ResetPasswordResult.Error -> when (result.dataError) {
                    // Expired/invalid token comes back as an auth-class error → full-screen
                    // recovery state with an exit, never a banner over a dead form (§4.6#1).
                    DataError.Remote.UNAUTHORIZED,
                    DataError.Remote.FORBIDDEN,
                    DataError.Remote.BAD_REQUEST,
                    -> updateState { copy(isLoading = false, isTokenInvalid = true) }

                    else -> updateState {
                        copy(isLoading = false, errorText = result.dataError.toUiText())
                    }
                }

                ResetPasswordResult.Success -> updateState {
                    copy(isLoading = false, isSuccess = true)
                }
            }
        }
    }
}
