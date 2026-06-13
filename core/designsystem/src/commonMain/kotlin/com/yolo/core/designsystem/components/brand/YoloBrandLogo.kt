package com.yolo.core.designsystem.components.brand


import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.logo_h
import org.jetbrains.compose.resources.vectorResource

@Composable
fun YoloBrandLogo(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
) {
    Icon(
        imageVector = vectorResource(Res.drawable.logo_h),
        contentDescription = null,
        tint = tint,
        modifier = modifier
    )
}
