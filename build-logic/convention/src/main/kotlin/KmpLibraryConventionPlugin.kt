import org.gradle.kotlin.dsl.configure
import com.yolo.myhabitshub.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.android.build.api.dsl.LibraryExtension
import com.yolo.myhabitshub.convention.configureKotlinMultiplatform
import com.yolo.myhabitshub.convention.libs
import com.yolo.myhabitshub.convention.pathToResourcePrefix

class KmpLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            configureKotlinMultiplatform()

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                resourcePrefix = this@with.pathToResourcePrefix()

                // Required to make debug build of app run in iOS simulator
                experimentalProperties["android.experimental.kmp.enableAndroidResources"] = "true"
            }

            dependencies {
                "commonMainImplementation"(libs.findLibrary("kotlinx-serialization-json").get())
                "commonTestImplementation"(libs.findLibrary("kotlin-test").get())
            }
        }
    }
}