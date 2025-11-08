import com.yolo.myhabitshub.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpPresentationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
       with(target){
           with(pluginManager){
               apply("com.yolo.convention.cmp.library")
           }

           dependencies {
               "commonMainImplementation"(project(":core:presentation"))
               "commonMainImplementation"(project(":core:designsystem"))

               "commonMainImplementation"(libs.findLibrary("koin-compose").get())
               "commonMainImplementation"(libs.findLibrary("koin-compose-viewmodel").get())
               "commonMainImplementation"(libs.findLibrary("koin-compose-viewmodel-navigation").get())

               "commonMainImplementation"(libs.findLibrary("jetbrains-compose-viewmodel").get())
               "commonMainImplementation"(libs.findLibrary("jetbrains-lifecycle-viewmodel").get())
               "commonMainImplementation"(libs.findLibrary("jetbrains-lifecycle-compose").get())
               "commonMainImplementation"(libs.findLibrary("jetbrains-lifecycle-viewmodel-savedstate").get())
               "commonMainImplementation"(libs.findLibrary("jetbrains-savedstate").get())
               "commonMainImplementation"(libs.findLibrary("jetbrains-bundle").get())
               "commonMainImplementation"(libs.findLibrary("jetbrains-navigation-compose").get())

               "androidMainImplementation"(libs.findLibrary("koin-android").get())
               "androidMainImplementation"(libs.findLibrary("koin-core-viewmodel").get())

           }
       }
    }
}