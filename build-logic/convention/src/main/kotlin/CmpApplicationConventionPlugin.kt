import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.yolo.myhabitshub.convention.configureAndroidLibraryTarget
import com.yolo.myhabitshub.convention.configureIosTargets
import com.yolo.myhabitshub.convention.libs
import com.yolo.myhabitshub.convention.pathToPackageName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class CmpApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                extensions.configure<KotlinMultiplatformAndroidLibraryExtension> {
                    compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
                    minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()
                    namespace = pathToPackageName()
                    experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
                    // buildConfig is not in KotlinMultiplatformAndroidLibraryExtension in some AGP versions
                    // or needs different configuration.
                }
            }

            configureAndroidLibraryTarget()
            configureIosTargets()

            dependencies {
                // Core Compose dependencies
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-runtime").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-foundation").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material3").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui").get())

                // CMP 1.10.0+: Resources and preview tooling are now separate modules
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-resources").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui-tooling-preview").get())

                // Single-variant model: use androidMainImplementation instead of debugImplementation
                "androidMainImplementation"(libs.findLibrary("jetbrains-compose-ui-tooling").get())
            }
        }
    }
}