package com.yolo.myhabitshub.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.yolo.myhabitshub.core.presentation.theme.AppTheme

@Composable
fun AutoResizableText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = AppTheme.typography.h1,
    color: Color = AppTheme.colors.text.primary,
    textAlign: TextAlign = TextAlign.Center,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
) {

    var resizeTextStyle by remember { mutableStateOf(style) }
    var shouldDrawText by remember { mutableStateOf(false) }
    Text(
        modifier = modifier.drawWithContent {
            if (shouldDrawText) drawContent()
        },
        text = text,
        style = resizeTextStyle,
        color = color,
        softWrap = false,
        textAlign = textAlign,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth) {
                resizeTextStyle = resizeTextStyle.copy(fontSize = resizeTextStyle.fontSize * 0.9)
            } else {
                shouldDrawText = true
            }
            onTextLayout?.invoke(textLayoutResult)
        }
    )
}