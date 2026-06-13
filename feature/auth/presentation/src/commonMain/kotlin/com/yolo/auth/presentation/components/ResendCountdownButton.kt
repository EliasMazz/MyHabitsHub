package com.yolo.auth.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.resend_in_x
import org.jetbrains.compose.resources.stringResource

/**
 * Resend button with the spec §5.1 cooldown UI: while [secondsRemaining] > 0 it renders
 * disabled with a live countdown ("Resend in 27s" — never a dead unlabeled button); at 0
 * it re-enables with [readyLabel]. The countdown itself is VM-owned (survives recomposition).
 */
@Composable
fun ResendCountdownButton(
    readyLabel: String,
    secondsRemaining: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    style: YoloButtonStyle = YoloButtonStyle.SECONDARY,
) {
    val coolingDown = secondsRemaining > 0
    YoloButton(
        text = if (coolingDown) {
            stringResource(Res.string.resend_in_x, secondsRemaining)
        } else {
            readyLabel
        },
        onClick = onClick,
        style = style,
        enabled = !coolingDown && !isLoading,
        isLoading = isLoading,
        modifier = modifier.fillMaxWidth(),
    )
}
