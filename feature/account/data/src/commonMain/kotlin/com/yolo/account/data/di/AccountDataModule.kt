package com.yolo.account.data.di

import com.yolo.account.data.repository.UserRepositoryImpl
import com.yolo.account.domain.repository.UserRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val accountDataModule = module {
    single { UserRepositoryImpl(applicationScope = get(), httpClient = get()) } bind UserRepository::class
}
