plugins {
    alias(libs.plugins.yolo.convention.kmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(projects.core.domain)
            }
        }

        androidMain {
            dependencies {
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}
