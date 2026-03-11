package com.yolo.myhabitshub.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.yolo.myhabitshub.util.LocalNativeViewFactory

@Composable
actual fun ExampleNativeTextView(text: String, modifier: Modifier) {

    val factory = LocalNativeViewFactory.current
    val view = remember(factory) { factory.createSwiftTextView(text) }

    UIKitViewController(
        modifier = modifier,
        factory = { view },
    )
}