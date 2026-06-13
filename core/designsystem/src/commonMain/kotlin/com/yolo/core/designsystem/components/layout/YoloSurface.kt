package com.yolo.core.designsystem.components.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.theme.YoloTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun YoloSurface(
    modifier: Modifier = Modifier,
    header: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    // One continuous plain surface; header/back-button zone + scrollable content.
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            header()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
                    // Shrinks content above the keyboard so the form actually scrolls.
                    .imePadding()
            ) {
                content()
            }
        }
    }
}

@Composable
@Preview
fun YoloSurfacePreview() {
    YoloTheme {
        YoloSurface(
            modifier = Modifier
                .fillMaxSize(),
            header = {
                YoloBrandLogo(modifier = Modifier.padding(vertical = 32.dp))
            },
            content = {
                Text(
                    text = "Welcome to Yolo!",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        )
    }
}
