package com.yolo.core.presentation.util

/**
 * True on Android, false on iOS. Used for platform-conditional UI
 * (e.g. which SSO provider button the auth Welcome screen shows).
 */
expect val isAndroidPlatform: Boolean
