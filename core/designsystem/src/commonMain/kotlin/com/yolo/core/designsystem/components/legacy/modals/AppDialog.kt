package com.yolo.core.designsystem.components.legacy.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import myhabitshub.core.designsystem.generated.resources.btn_ok
import myhabitshub.core.designsystem.generated.resources.title_error_dialog
import myhabitshub.core.designsystem.generated.resources.title_info
import com.yolo.core.designsystem.components.legacy.AppButton
import com.yolo.core.designsystem.components.legacy.ButtonStyle
import com.yolo.core.designsystem.components.legacy.DialogOrBottomSheetTitle
import com.yolo.core.designsystem.components.legacy.PreviewHelper
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_check
import myhabitshub.core.designsystem.generated.resources.ic_close
import myhabitshub.core.designsystem.generated.resources.Res as R
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


enum class DialogType {
    INFO,
    ERROR,
}

@Composable
fun AppDialog(
    type: DialogType = DialogType.INFO,
    title: String = when (type) {
        DialogType.INFO -> stringResource(Res.string.title_info)
        DialogType.ERROR -> stringResource(Res.string.title_error_dialog)
    },
    titleColor: Color = when (type) {
        DialogType.INFO -> MaterialTheme.colorScheme.primary
        DialogType.ERROR -> MaterialTheme.colorScheme.error
    },
    text: String? = "",
    image: @Composable (() -> Unit)? = { AppDialogImage(dialogType = type) },
    btnConfirmText: String = stringResource(Res.string.btn_ok),
    btnDismissText: String = "",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) = AppDialog(
    title = title,
    titleColor = titleColor,
    text = text,
    image = image,
    btnConfirmText = btnConfirmText,
    btnDismissText = btnDismissText,
    onConfirm = onConfirm,
    onDismiss = onDismiss
)


@Composable
fun AppDialog(
    title: String = stringResource(Res.string.title_info),
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    text: String? = "",
    image: @Composable (() -> Unit)? = null,
    btnConfirmText: String = stringResource(Res.string.btn_ok),
    btnDismissText: String = "",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (text.isNullOrEmpty()) return

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.8f), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.extended.surfaceHigher,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(YoloTokens.spacing.dialogPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.dialogPadding)
            ) {

                image?.let {
                    Box(contentAlignment = Alignment.Center) { it() }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DialogOrBottomSheetTitle(
                        color = titleColor,
                        text = title,
                        textAlign = TextAlign.Center
                    )
                    if (text.isEmpty().not()) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }

                }
                Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap)) {
                    AppButton(
                        text = btnConfirmText.ifEmpty { stringResource(Res.string.btn_ok) },
                        onClick = { onConfirm() },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (btnDismissText.isNotEmpty()) {
                        AppButton(
                            btnDismissText,
                            onClick = { onDismiss() },
                            style = ButtonStyle.ALTERNATIVE,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }

            }

        }
    }
}


@Composable
fun AppDialogImage(
    dialogType: DialogType,
    icon: DrawableResource = when (dialogType) {
        DialogType.INFO -> R.drawable.ic_check
        DialogType.ERROR -> R.drawable.ic_close
    }
) {
    val iconTint = when (dialogType) {
        DialogType.INFO -> MaterialTheme.colorScheme.primary
        DialogType.ERROR -> MaterialTheme.colorScheme.error
    }

    Box(
        modifier = Modifier
            .padding(top = YoloTokens.spacing.elementGap)
            .size(114.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(listOf(iconTint, iconTint.copy(0.8f)))
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(YoloTokens.spacing.elementGap),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconTint
            )
        }

    }

}


@Preview
@Composable
fun AppDialogPreview() {
    PreviewHelper {
        var dialogType by remember { mutableStateOf<DialogType?>(null) }
        androidx.compose.material3.Button(
            onClick = {
                dialogType = DialogType.INFO
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.extended.infoContainer,
                contentColor = MaterialTheme.colorScheme.extended.info
            )
        ) {
            Text("Show Info Dialog")
        }
        androidx.compose.material3.Button(
            onClick = {
                dialogType = DialogType.ERROR
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Show Error Dialog")
        }

        dialogType?.let {
            AppDialog(
                type = it,
                text = "App Dialog Body text",
                onDismiss = {
                    dialogType = null
                },
                onConfirm = {
                    dialogType = null
                },
            )
        }
    }
}

