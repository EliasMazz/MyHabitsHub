package com.yolo.myhabitshub.data.source.featureflag

interface FeatureFlagManager {

    object Keys {
        const val IS_ANALYTICS_ENABLED = "is_analytics_enabled"
    }

    companion object {
        //Add Optional Default Feature Flag Values Here
        val DEFAULT_VALUES = mapOf(
            Keys.IS_ANALYTICS_ENABLED to true,
        )
    }

    fun syncsFlagsAsync()
    fun getBoolean(key: String): Boolean
    fun getString(key: String): String
    fun getLong(key: String): Long
    fun getDouble(key: String): Double
}