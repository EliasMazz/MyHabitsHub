package com.yolo.core.presentation

import androidx.compose.runtime.compositionLocalOf
import com.yolo.core.presentation.navigation.Navigator

val LocalNavigator = compositionLocalOf<Navigator> {
    error("No LocalNavController provided")
}
