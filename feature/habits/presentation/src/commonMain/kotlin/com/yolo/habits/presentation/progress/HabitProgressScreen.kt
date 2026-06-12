package com.yolo.habits.presentation.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yolo.core.presentation.BaseScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HabitProgressScreen(
    viewModel: HabitProgressViewModel = koinViewModel(),
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            // Handle events if any
        }
    ) { state, onIntent ->
        HabitProgressScreenContent(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun HabitProgressScreenContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Habits Progress Screen")
    }
}