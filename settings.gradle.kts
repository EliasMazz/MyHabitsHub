rootProject.name = "MyHabitsHub"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":shared")

include(":core:presentation")
include(":core:domain")
include(":core:data")
include(":core:designsystem")
include(":core:catalog")
include(":feature:auth:presentation")
include(":feature:auth:domain")
include(":feature:coach:presentation")
include(":feature:coach:domain")
include(":feature:coach:data")
include(":feature:habits:presentation")
include(":feature:habits:domain")
include(":feature:account:presentation")
include(":feature:account:domain")
include(":feature:account:data")
include(":androidApp")
