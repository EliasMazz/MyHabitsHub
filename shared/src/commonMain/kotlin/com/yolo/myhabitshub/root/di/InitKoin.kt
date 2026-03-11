package com.yolo.myhabitshub.root.di

import com.russhwolf.settings.Settings
import com.yolo.auth.domain.di.authDomainModule
import com.yolo.auth.presentation.di.authPresentationModule
import com.yolo.core.data.di.coreDataModule
import com.yolo.core.domain.validator.PasswordValidatorUseCase
import com.yolo.myhabitshub.data.repository.UserRepository
import com.yolo.myhabitshub.data.repository.UserRepositoryImpl
import com.yolo.myhabitshub.data.source.preferences.UserPreferences
import com.yolo.myhabitshub.data.source.preferences.UserPreferencesImpl
import com.yolo.myhabitshub.data.source.remote.HttpClientFactoryLegacy
import com.yolo.myhabitshub.data.source.remote.apiservices.ApiService
import com.yolo.myhabitshub.domain.usecase.DeleteAccountUseCase
import com.yolo.myhabitshub.domain.usecase.GetUserStream
import com.yolo.myhabitshub.domain.usecase.LogOutUseCase
import com.yolo.myhabitshub.domain.usecase.SendAuthTokenUseCase
import com.yolo.myhabitshub.presentation.screens.account.AccountViewModel
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportViewModel
import com.yolo.myhabitshub.presentation.screens.main.MainViewModel
import com.yolo.myhabitshub.presentation.screens.onboarding.OnBoardingViewModel
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressViewModel
import com.yolo.myhabitshub.presentation.screens.settings.SettingsViewModel
import com.yolo.myhabitshub.presentation.screens.signin.SignInViewModel
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingViewModel
import com.yolo.myhabitshub.util.ApplicationScope
import com.yolo.myhabitshub.util.platformModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

private val domainModule = module {
    factory { GetUserStream(get()) }
    factory { DeleteAccountUseCase(get()) }
    factory { LogOutUseCase(get()) }
    factory { SendAuthTokenUseCase(get()) }
    factory { PasswordValidatorUseCase() }
}

private val dataModule = module {
    singleOf(::ApplicationScope)
    factory { Dispatchers.IO } bind CoroutineContext::class

    //Preferences Source
    single { Settings() } bind Settings::class
    singleOf(::UserPreferencesImpl) bind UserPreferences::class

    //Remote source
    single { HttpClientFactoryLegacy.default() }
    factoryOf(::ApiService)

    //Repositories
    single { UserRepositoryImpl(get(), get()) } bind UserRepository::class
}

private val presentationModule = module {
    viewModelOf(::OnBoardingViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::HelpAndSupportViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::HabitTrackingViewModel)
    viewModelOf(::HabitProgressViewModel)
    viewModelOf(::MainViewModel)
}

val appModules: List<Module> get() = platformModule + domainModule + dataModule + presentationModule + authModule
private val authModule: List<Module> get() = coreDataModule + authDomainModule + authPresentationModule