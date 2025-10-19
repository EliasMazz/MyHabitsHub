package com.yolo.myhabitshub.util

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yolo.myhabitshub.BuildConfig
import com.yolo.myhabitshub.data.source.featureflag.FeatureFlagManager
import com.yolo.myhabitshub.data.source.featureflag.FeatureFlagManagerImpl
import com.yolo.myhabitshub.util.analytics.Analytics
import com.yolo.myhabitshub.util.analytics.FirebaseAnalyticsImpl
import com.yolo.myhabitshub.util.inappreview.InAppReviewManager
import com.yolo.myhabitshub.util.inappreview.InAppReviewManagerImpl
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    factoryOf(::InAppReviewManagerImpl) bind InAppReviewManager::class
    factoryOf(::AppUtilImpl) bind AppUtil::class
    single<FeatureFlagManagerImpl> {
        val remoteConfig = Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings {
                // set minimumFetchIntervalInSeconds to 0 to get fresh updates in debug mode for testing
                if (BuildConfig.DEBUG) minimumFetchIntervalInSeconds = 3600
            })
            setDefaultsAsync(FeatureFlagManager.DEFAULT_VALUES)
        }
        FeatureFlagManagerImpl(remoteConfig = remoteConfig)
    } bind FeatureFlagManager::class
    single { FirebaseAnalyticsImpl(firebaseAnalytics = Firebase.analytics) } bind Analytics::class
}

internal actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = android.R.drawable.ic_menu_compass
        )
    )

}

internal actual val isAndroid = true
internal actual val isDebug = BuildConfig.DEBUG