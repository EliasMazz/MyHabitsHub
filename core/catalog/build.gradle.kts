plugins {
    alias(libs.plugins.yolo.convention.cmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(projects.core.designsystem)
            }
        }
    }
}
