package com.yolo.core.presentation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavigator = compositionLocalOf<NavHostController> {
    error("No LocalNavController provided")
}
