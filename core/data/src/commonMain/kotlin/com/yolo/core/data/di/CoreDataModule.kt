package com.yolo.core.data.di

import com.yolo.core.data.auth.AuthRepositoryImpl
import com.yolo.core.data.networking.HttpClientFactory
import com.yolo.core.domain.auth.AuthRepository
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)

    single {
        HttpClientFactory().create(get())
    }
    single { AuthRepositoryImpl(get()) } bind AuthRepository::class
}