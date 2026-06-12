package com.yolo.account.domain.di

import com.yolo.account.domain.usecase.DeleteAccountUseCase
import com.yolo.account.domain.usecase.GetUserStream
import com.yolo.account.domain.usecase.LogOutUseCase
import com.yolo.account.domain.usecase.SendAuthTokenUseCase
import org.koin.dsl.module

val accountDomainModule = module {
    factory { GetUserStream(get()) }
    factory { DeleteAccountUseCase(get()) }
    factory { LogOutUseCase(get()) }
    factory { SendAuthTokenUseCase(get()) }
}
