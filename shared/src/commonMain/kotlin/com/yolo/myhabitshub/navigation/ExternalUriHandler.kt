package com.yolo.myhabitshub.navigation

object ExternalUriHandler {

    private var cached: String? = null

    var listener: ((uri: String) -> Unit)? = null
        set(value) {
            field = value
            if(value != null) {
                cached?.let {
                    println("[DEBUG] ExternalUriHandler: replaying cached URI: $it")
                    value.invoke(it)
                }
                cached = null
            }
        }

    fun onNewUri(uri: String) {
        println("[DEBUG] ExternalUriHandler.onNewUri() called with: $uri")
        cached = uri
        listener?.let {
            println("[DEBUG] ExternalUriHandler: listener is set, invoking with: $uri")
            it.invoke(uri)
            cached = null
        } ?: println("[DEBUG] ExternalUriHandler: listener is null, URI cached")
    }
}