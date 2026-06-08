plugins {
    alias(libs.plugins.yolo.convention.cmp.library)
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.material3.adaptive)
                implementation(projects.core.domain)
                implementation(compose.components.resources)
                api(libs.jetbrains.lifecycle.viewmodel)
                api(libs.jetbrains.lifecycle.compose)
                api(libs.jetbrains.compose.viewmodel)
                implementation(libs.jetbrains.navigation.compose)
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