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