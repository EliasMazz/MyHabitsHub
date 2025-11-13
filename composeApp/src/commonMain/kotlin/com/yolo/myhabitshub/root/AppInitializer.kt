package com.yolo.myhabitshub.root

import com.yolo.myhabitshub.data.repository.UserRepository
import com.yolo.myhabitshub.data.source.featureflag.FeatureFlagManager
import com.yolo.myhabitshub.data.source.preferences.UserPreferences
import com.yolo.myhabitshub.data.source.preferences.UserPreferencesImpl
import com.yolo.myhabitshub.data.source.remote.HttpClientFactory
import com.yolo.myhabitshub.data.source.remote.apiservices.ApiService
import com.yolo.myhabitshub.presentation.screens.account.AccountViewModel
import com.yolo.myhabitshub.presentation.screens.onboarding.OnBoardingViewModel
import com.yolo.myhabitshub.presentation.screens.settings.SettingsViewModel
import com.yolo.myhabitshub.util.ApplicationScope
import com.yolo.myhabitshub.util.analytics.Analytics
import com.yolo.myhabitshub.util.isDebug
import com.yolo.myhabitshub.util.onApplicationStartPlatformSpecific
import com.yolo.myhabitshub.util.platformModule
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.russhwolf.settings.Settings
import com.yolo.auth.domain.EmailValidatorUseCase
import com.yolo.auth.domain.RegisterValidatorUseCase
import com.yolo.auth.presentation.RegisterViewModel
import com.yolo.myhabitshub.common.BuildConfig
import com.yolo.myhabitshub.data.repository.UserRepositoryImpl
import com.yolo.myhabitshub.domain.usecase.DeleteAccountUseCase
import com.yolo.myhabitshub.domain.usecase.GetUserStream
import com.yolo.myhabitshub.domain.usecase.LogOutUseCase
import com.yolo.myhabitshub.domain.usecase.SendAuthTokenUseCase
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportViewModel
import com.yolo.myhabitshub.presentation.screens.main.MainViewModel
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressViewModel
import com.yolo.myhabitshub.presentation.screens.signin.SignInViewModel
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingViewModel
import com.yolo.core.data.logging.AppLogger
import com.yolo.core.domain.validator.PasswordValidatorUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

object AppInitializer {

    fun initialize(onKoinStart: KoinApplication.() -> Unit) {
        startKoin {
            onKoinStart()
            modules(appModules)
            onApplicationStart()
        }
    }

    private fun KoinApplication.onApplicationStart() {
        onApplicationStartPlatformSpecific()
        AppLogger.initialize(isDebug = isDebug)
        refreshFeatureFlags()
        initializeAnalytics()
        initializeNotification()
        initializeAuthentication()
    }
}

private fun KoinApplication.refreshFeatureFlags() {
    val featureFlagManager by this.koin.inject<FeatureFlagManager>()
    featureFlagManager.syncsFlagsAsync()
}

private fun KoinApplication.initializeAnalytics() {
    val featureFlagManager by this.koin.inject<FeatureFlagManager>()
    val analytics by this.koin.inject<Analytics>()
    val isAnalyticsEnabled =
        featureFlagManager.getBoolean(FeatureFlagManager.Keys.IS_ANALYTICS_ENABLED)
    analytics.setEnabled(enabled = isAnalyticsEnabled)
}

private fun initializeNotification() {
    NotifierManager.addListener(object : NotifierManager.Listener {

        /**
         * This method is called when a new FCM token is generated.
         * You can use this token for sending notifications to the specific device or saving in the server.
         * It is logged for debugging purposes.
         */
        override fun onNewToken(token: String) {
            super.onNewToken(token)
            AppLogger.d("Firebase onNewToken: $token")
        }

        /**
         * This method is invoked when the user clicks on a notification.
         * @param data parameter contains the payload data sent with the notification
         */
        override fun onNotificationClicked(data: PayloadData) {
            super.onNotificationClicked(data)
            AppLogger.d("onNotification clicked: $data")

        }

        /**
         * This method is invoked when receiving a data type notification.
         * @param data parameter contains the payload data sent with the notification
         */
        override fun onPayloadData(data: PayloadData) {
            super.onPayloadData(data)
            AppLogger.d("Firebase notification onPayloadData: $data")

        }

        /**
         * This method is invoked when receiving a notification
         */
        override fun onPushNotification(title: String?, body: String?) {
            super.onPushNotification(title, body)
            AppLogger.d("Firebase onPushNotification: title: $title, body: $body")
        }
    })
}

private fun initializeAuthentication() {
    GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = BuildConfig.GOOGLE_WEB_CLIENT_ID))
}

private val domainModule = module {
    factory { GetUserStream(get()) }
    factory { DeleteAccountUseCase(get()) }
    factory { LogOutUseCase(get()) }
    factory { SendAuthTokenUseCase(get()) }
    factory { EmailValidatorUseCase() }
    factory { PasswordValidatorUseCase() }
    factory { RegisterValidatorUseCase(get(), get()) }
}

private val dataModule = module {
    singleOf(::ApplicationScope)
    factory { Dispatchers.IO } bind CoroutineContext::class

    //Preferences Source
    single { Settings() } bind Settings::class
    singleOf(::UserPreferencesImpl) bind UserPreferences::class

    //Remote source
    single { HttpClientFactory.default() }
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
    viewModelOf(::RegisterViewModel)
}

private val appModules: List<Module> get() = platformModule + domainModule + dataModule + presentationModule