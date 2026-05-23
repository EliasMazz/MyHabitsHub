package com.yolo.myhabitshub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.NavUri

@Composable
fun DeepLinkListener(
    navController: NavController
) {
    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            println("[DEBUG] DeepLinkListener: navigating to URI: $uri")
            navController.navigate(NavUri(uri))
        }

        onDispose {
            ExternalUriHandler.listener = null
        }
    }
}
