package com.yolo.habits.presentation.di

import com.yolo.habits.presentation.progress.HabitProgressViewModel
import com.yolo.habits.presentation.tracking.HabitTrackingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val habitsPresentationModule = module {
    viewModelOf(::HabitTrackingViewModel)
    viewModelOf(::HabitProgressViewModel)
}
