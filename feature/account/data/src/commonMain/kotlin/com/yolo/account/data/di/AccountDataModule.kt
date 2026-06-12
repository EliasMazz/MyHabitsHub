package com.yolo.account.data.di

import com.yolo.account.data.remote.HttpClientFactoryLegacy
import com.yolo.account.data.remote.apiservices.ApiService
import com.yolo.account.data.repository.UserRepositoryImpl
import com.yolo.account.domain.repository.UserRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val accountDataModule = module {
    single { HttpClientFactoryLegacy.default() }
    factoryOf(::ApiService)

    single { UserRepositoryImpl(get(), get()) } bind UserRepository::class
}
