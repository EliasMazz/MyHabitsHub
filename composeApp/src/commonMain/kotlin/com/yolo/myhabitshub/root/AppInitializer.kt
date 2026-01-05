package com.yolo.myhabitshub.root

import com.yolo.myhabitshub.data.source.featureflag.FeatureFlagManager
import com.yolo.myhabitshub.util.analytics.Analytics
import com.yolo.myhabitshub.util.isDebug
import com.yolo.myhabitshub.util.onApplicationStartPlatformSpecific
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.yolo.myhabitshub.common.BuildConfig
import com.yolo.core.data.logging.AppLogger
import com.yolo.myhabitshub.root.di.appModules
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

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
