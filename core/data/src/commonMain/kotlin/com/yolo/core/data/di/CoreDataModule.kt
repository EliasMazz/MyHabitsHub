package com.yolo.core.data.di

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.yolo.core.data.auth.AuthRepositoryImpl
import com.yolo.core.data.auth.SettingsSessionStorage
import com.yolo.core.data.networking.HttpClientFactory
import com.yolo.core.data.preferences.UserPreferencesImpl
import com.yolo.core.data.util.ApplicationScope
import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.auth.SessionStorage
import com.yolo.core.domain.preferences.UserPreferences
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)

    single { HttpClientFactory(sessionStorage = get()).create(engine = get()) }
    single { AuthRepositoryImpl(httpClient = get()) } bind AuthRepository::class
    single<SessionStorage> { SettingsSessionStorage(get<ObservableSettings>()) }

    singleOf(::ApplicationScope)
    single<Settings> { Settings() }
    singleOf(::UserPreferencesImpl) bind UserPreferences::class
}