package com.yolo.myhabitshub.util

import com.yolo.myhabitshub.data.source.featureflag.FeatureFlagManager
import com.yolo.myhabitshub.util.analytics.Analytics
import com.yolo.myhabitshub.util.inappreview.InAppReviewManager
import com.yolo.myhabitshub.util.inappreview.InAppReviewManagerImpl
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.experimental.ExperimentalNativeApi

internal actual val platformModule: Module = module {
    factoryOf(::InAppReviewManagerImpl) bind InAppReviewManager::class
    factoryOf(::AppUtilImpl) bind AppUtil::class
}

internal fun swiftLibDependenciesModule(factory: SwiftLibDependencyFactory): Module = module {
    single { factory.provideFeatureFlagManagerImpl() } bind FeatureFlagManager::class
    single { factory.provideFirebaseAnalyticsImpl() } bind Analytics::class
}

internal actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(NotificationPlatformConfiguration.Ios())

}

internal actual val isAndroid = false

@OptIn(ExperimentalNativeApi::class)
internal actual val isDebug = Platform.isDebugBinary