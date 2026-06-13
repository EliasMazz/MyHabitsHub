package com.yolo.myhabitshub.app.di

import com.yolo.account.data.di.accountDataModule
import com.yolo.account.domain.di.accountDomainModule
import com.yolo.account.presentation.di.accountPresentationModule
import com.yolo.auth.domain.di.authDomainModule
import com.yolo.auth.presentation.di.authPresentationModule
import com.yolo.core.data.di.coreDataModule
import com.yolo.core.domain.validator.PasswordValidatorUseCase
import com.yolo.habits.presentation.di.habitsPresentationModule
import com.yolo.myhabitshub.app.AppViewModel
import com.yolo.myhabitshub.presentation.screens.main.MainViewModel
import com.yolo.myhabitshub.util.platformModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

/**
 * App-shell bindings owned by the umbrella `shared` module only.
 * Feature bindings live in each feature's own di package.
 */
private val appModule = module {
    factory { Dispatchers.IO } bind CoroutineContext::class
    factory { PasswordValidatorUseCase() }

    viewModelOf(::MainViewModel)
    viewModelOf(::AppViewModel)
}

val appModules: List<Module> get() = platformModule + appModule + coreModule + authModule + habitsModule + accountModule

private val coreModule: List<Module> get() = listOf(coreDataModule)
private val authModule: List<Module> get() = authDomainModule + authPresentationModule
private val habitsModule: List<Module> get() = listOf(habitsPresentationModule)
private val accountModule: List<Module> get() = accountDomainModule + accountDataModule + accountPresentationModule
