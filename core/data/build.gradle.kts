plugins {
    alias(libs.plugins.yolo.convention.kmp.library)
    alias(libs.plugins.yolo.convention.buildkonfig)
}

kotlin {
    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(libs.ktor.core)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.content.negotiation )
                implementation(libs.ktor.serialization.kotlinx.json )

                implementation(libs.napier)

                implementation(projects.core.domain)
                implementation(libs.koin.core)
            }
        }

        androidMain {
            dependencies {
              implementation(libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

