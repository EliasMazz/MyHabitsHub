package com.yolo.myhabitshub.convention

import org.gradle.api.Project
import java.util.Locale

private const val BASE_PATH = "com.yolo.myhabitshub"
fun Project.pathToPackageName(): String {
    val relativePackageName = path
        .replace(':', '.')
        .lowercase()

    return  "$BASE_PATH$relativePackageName"
}

fun Project.pathToResourcePrefix(): String {
    return path
        .replace(':', '_')
        .drop(1) + "_"
        .lowercase()
}

fun Project.pathToFrameworkName () : String{
    val parts = this.path.split(":", "-", "_", " ")
    return parts.joinToString("") { part ->
        part.replaceFirstChar {
            it.titlecase(Locale.ROOT)
        }
    }
}