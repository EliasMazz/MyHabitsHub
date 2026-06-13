package com.yolo.core.designsystem.components.textfields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.eye_icon
import myhabitshub.core.designsystem.generated.resources.eye_off_icon
import myhabitshub.core.designsystem.generated.resources.hide_password
import myhabitshub.core.designsystem.generated.resources.show_password
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun YoloPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onToggleVisibilityClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    title: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    onFocusChanged: (Boolean) -> Unit = {},
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: (() -> Unit)? = null,
    contentType: ContentType? = null,
) {
    YoloTextFieldLayout(
        title = title,
        isError = isError,
        supportingText = supportingText,
        enabled = enabled,
        onFocusChanged = onFocusChanged,
        modifier = modifier
    ) { styleModifier, interactionSource ->
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = styleModifier.semantics {
                contentType?.let { this.contentType = it }
            },
            enabled = enabled,
            singleLine = true,
            visualTransformation = if(isPasswordVisible) {
                VisualTransformation.None
            } else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction,
            ),
            keyboardActions = if (onImeAction != null) {
                KeyboardActions(
                    onDone = { onImeAction() },
                    onGo = { onImeAction() },
                    onNext = { onImeAction() },
                    onSend = { onImeAction() },
                )
            } else {
                KeyboardActions.Default
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = if(enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.extended.textPlaceholder
                }
            ),
            interactionSource = interactionSource,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            decorationBox = { innerBox ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = YoloTokens.sizing.minTouchTarget),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if(value.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                color = MaterialTheme.colorScheme.extended.textPlaceholder,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        innerBox()
                    }

                    Box(
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(
                                    bounded = false,
                                    radius = 24.dp
                                ),
                                onClick = onToggleVisibilityClick
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            // State convention: open eye while visible, slashed while hidden.
                            imageVector = if(isPasswordVisible) {
                                vectorResource(Res.drawable.eye_icon)
                            } else {
                                vectorResource(Res.drawable.eye_off_icon)
                            },
                            contentDescription = if(isPasswordVisible) {
                                stringResource(Res.string.hide_password)
                            } else {
                                stringResource(Res.string.show_password)
                            },
                            tint = MaterialTheme.colorScheme.extended.textDisabled,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun YoloPasswordTextFieldEmptyPreview() {
    YoloTheme {
        YoloPasswordTextField(
            value = "",
            onValueChange = {},
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            modifier = Modifier
                .width(300.dp),
            placeholder = "Password",
            title = "Password",
            supportingText = "Use 6+ characters, at least one digit",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun YoloPasswordTextFieldFilledPreview() {
    YoloTheme {
        YoloPasswordTextField(
            value = "password123",
            onValueChange = {},
            isPasswordVisible = false,
            onToggleVisibilityClick = {},
            modifier = Modifier
                .width(300.dp),
            placeholder = "Password",
            title = "Password",
            supportingText = "Use 6+ characters, at least one digit",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun YoloPasswordTextFieldErrorPreview() {
    YoloTheme {
        YoloPasswordTextField(
            value = "password123",
            onValueChange = {},
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            modifier = Modifier
                .width(300.dp),
            placeholder = "Password",
            title = "Password",
            supportingText = "Doesn't contain an uppercase character",
            isError = true,
        )
    }
}