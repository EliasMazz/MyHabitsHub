plugins {
    alias(libs.plugins.yolo.convention.cmp.presentation)
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.feature.auth.domain)
                implementation(projects.core.domain)
                implementation(projects.core.designsystem)
                implementation(projects.core.presentation)

                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
            }
        }

        androidMain {
            dependencies {
                // Native Google Sign-In via AndroidX Credential Manager (no Firebase).
                implementation(libs.androidx.credentials)
                implementation(libs.androidx.credentials.play.services.auth)
                implementation(libs.google.identity.googleid)
            }
        }

        iosMain {
            dependencies {
                // Apple Sign-In uses the native AuthenticationServices framework via Kotlin/Native
                // interop (no extra dependency required).
            }
        }
    }

}