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
                implementation(projects.feature.account.domain)
                implementation(projects.core.domain)
                implementation(projects.core.data)

                implementation(libs.kmpauth.firebase)
                implementation(libs.ktor.core)
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
