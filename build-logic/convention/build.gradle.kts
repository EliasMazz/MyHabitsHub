import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.myhabitshub.convention.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    implementation(libs.buildkonfig.gradlePlugin)
    implementation(libs.buildkonfig.compiler)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions{
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks{
    validatePlugins{
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "com.yolo.convention.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidComposeApplication") {
            id = "com.yolo.convention.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("cmpApplication") {
            id = "com.yolo.convention.cmp.application"
            implementationClass = "CmpApplicationConventionPlugin"
        }
        register("kmpLibrary") {
            id = "com.yolo.convention.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("cmpLibrary") {
            id = "com.yolo.convention.cmp.library"
            implementationClass = "CmpLibraryConventionPlugin"
        }
        register("cmpPresentation") {
            id = "com.yolo.convention.cmp.presentation"
            implementationClass = "CmpPresentationConventionPlugin"
        }
        register("buildkonfig") {
            id = "com.yolo.convention.buildkonfig"
            implementationClass = "BuildKonfigConventionPlugin"
        }
    }
}