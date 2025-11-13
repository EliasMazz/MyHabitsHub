package com.yolo.myhabitshub.root

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.presentation.util.UiText
import com.yolo.myhabitshub.util.extensions.ObserveFlowAsEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
internal fun App() {
    YoloTheme {
        AppNavigation()
    }
    /*
    YoloTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Set this to true for showing app, or false for ui components gallery
            val isPreviewComponentsMode = false
            if (isPreviewComponentsMode) AllComponentsGallery()
            else
                AppScaffold()
        }
    }
     */
}

@Composable
private fun AppScaffold() {
    val snackbarHostState = remember { SnackbarHostState() }
    var uiText by remember { mutableStateOf<UiText?>(null) }

    ObserveFlowAsEvent(AppGlobalUiState.uiMessageFlow) { uiText = it }

    uiText?.value?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
            uiText = null
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        AppNavigation()
    }
}

object AppGlobalUiState {
    private val uiTextChannel = Channel<UiText>(Channel.BUFFERED)
    val uiMessageFlow = uiTextChannel.receiveAsFlow()

    fun showUiMessage(uiText: UiText) {
        uiTextChannel.trySend(uiText)
    }
}