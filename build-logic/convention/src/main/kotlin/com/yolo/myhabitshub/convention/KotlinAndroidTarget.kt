package com.yolo.myhabitshub.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidLibraryTarget() {
   dependencies{
       "coreLibraryDesugaring"(libs.findLibrary("android-desugarJdkLibs").get())
   }
}