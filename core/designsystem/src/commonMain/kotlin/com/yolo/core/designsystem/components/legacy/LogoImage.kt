package com.yolo.core.designsystem.components.legacy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.components.brand.YoloBrandLogo

@Composable
fun LogoImage(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Theme-aware brand mark (replaces the legacy ic_logo raster).
        YoloBrandLogo(modifier = Modifier.size(140.dp).align(Alignment.Center))
    }
}
