package com.yolo.core.designsystem.components.legacy.modals

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.components.legacy.AppButton
import com.yolo.core.designsystem.components.legacy.PreviewHelper
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomDialog(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {

    AppDialog(
        title = "Title",
        text = "Description",
        image = { YoloBrandLogo() },
        btnConfirmText = "Confirm",
        btnDismissText = "Dismiss",
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Preview
@Composable
fun DialogPreview() {
    var isDialogVisible by remember { mutableStateOf(false) }
    AppButton("Show Dialog", onClick = { isDialogVisible = true })
    PreviewHelper {
        if (isDialogVisible) {
            CustomDialog(
                onConfirm = { isDialogVisible = false },
                onDismiss = { isDialogVisible = false }
            )
        }

    }
}