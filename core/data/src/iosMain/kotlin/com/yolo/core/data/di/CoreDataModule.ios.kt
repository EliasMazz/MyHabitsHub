package com.yolo.core.data.di

import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.ObservableSettings
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformCoreDataModule : Module = module {
    single <HttpClientEngine> { Darwin.create() }
    single<ObservableSettings> { KeychainSettings(service = "MyHabitsHubService") }
}