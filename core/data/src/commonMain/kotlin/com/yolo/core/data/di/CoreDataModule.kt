package com.yolo.core.data.di

import com.russhwolf.settings.ObservableSettings
import com.yolo.core.data.auth.AuthRepositoryImpl
import com.yolo.core.data.auth.SettingsSessionStorage
import com.yolo.core.data.networking.HttpClientFactory
import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.auth.SessionStorage
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)

    single { HttpClientFactory(sessionStorage = get()).create(engine = get()) }
    single { AuthRepositoryImpl(httpClient = get()) } bind AuthRepository::class
    single<SessionStorage> { SettingsSessionStorage(get<ObservableSettings>()) }
}