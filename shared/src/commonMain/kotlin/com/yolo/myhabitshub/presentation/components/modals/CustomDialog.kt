package com.yolo.myhabitshub.presentation.components.modals

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.yolo.myhabitshub.presentation.components.AppButton
import com.yolo.myhabitshub.presentation.components.PreviewHelper
import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomDialog(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {

    AppDialog(
        title = "Title",
        text = "Description",
        image = {
            Image(
                painter = painterResource(Res.drawable.ic_logo),
                contentDescription = null,
            )
        },
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