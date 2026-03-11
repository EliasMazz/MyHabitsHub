package com.yolo.myhabitshub.util

import com.yolo.myhabitshub.data.source.featureflag.FeatureFlagManager
import com.yolo.myhabitshub.util.analytics.Analytics

/**
This factory is used to help to use swift libraries in KMP. Actual implementations are provided in swift.
 */
interface SwiftLibDependencyFactory {
    fun provideFeatureFlagManagerImpl(): FeatureFlagManager
    fun provideFirebaseAnalyticsImpl(): Analytics
}