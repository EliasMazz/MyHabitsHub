plugins {
    alias(libs.plugins.yolo.convention.cmp.presentation)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.feature.account.domain)
                implementation(projects.core.domain)
                implementation(projects.core.data)
                implementation(projects.core.designsystem)
                implementation(projects.core.presentation)

                implementation(libs.kmpauth.google)
                implementation(libs.kmpauth.firebase)
                implementation(libs.kmpauth.uihelper)

                implementation(libs.coil.compose)
                implementation(libs.coil.ktor)

                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
            }
        }

        androidMain {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.bom))
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}
