package com.yolo.core.data.networking

actual object UrlConstants {
    // iOS Simulator can reach the Mac host via localhost. For physical device testing,
    // replace with your machine's LAN IP (e.g., http://192.168.1.100:8080).
    actual val BASE_URL_HTTP: String = "http://localhost:8080"
}
