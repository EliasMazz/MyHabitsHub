package com.yolo.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Design System v3 shape scale — exact M3 Expressive ShapeTokens corner values
// (design-system-v3-spec §6.1). Component bindings: buttons = full pill (CircleShape),
// text fields = medium, sheets (top corners) / dialogs = extraLarge.
val YoloShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
