import com.android.build.api.dsl.ApplicationExtension
import com.yolo.myhabitshub.convention.configureKotlinAndroid
import com.yolo.myhabitshub.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
       with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

           extensions.configure<ApplicationExtension>(){
               namespace = "com.yolo.myhabitshub"

               defaultConfig {
                   applicationId = libs.findVersion("projectApplicationId").get().toString()
                   targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()
                   versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                   versionName = libs.findVersion("projectVersionName").get().toString()
               }

               val keystorePropertiesFile = rootProject.file("distribution/android/keystore/keystore.properties")
               val isSigningKeyExists = keystorePropertiesFile.exists()
               val keystoreProperties = Properties()
               if (isSigningKeyExists) keystorePropertiesFile.inputStream().use { keystoreProperties.load(it) }

               signingConfigs {
                   if (isSigningKeyExists)
                       create("release") {
                           val storeFilePath = keystoreProperties.getProperty("storeFile")
                           storeFile = rootProject.file(storeFilePath)
                           storePassword = keystoreProperties.getProperty("keystorePassword")
                           keyAlias = keystoreProperties.getProperty("keyAlias")
                           keyPassword = keystoreProperties.getProperty("keyPassword")
                       }
               }

               buildTypes {
                   getByName("debug") {
                       isMinifyEnabled = false
                       isDebuggable = true
                   }

                   getByName("release") {
                       isMinifyEnabled = true
                       isShrinkResources = true
                       proguardFiles(
                           getDefaultProguardFile("proguard-android.txt"),
                           "proguard-rules.pro"
                       )
                       signingConfig = signingConfigs.getByName(if (isSigningKeyExists) "release" else "debug")
                   }
               }

               buildFeatures {
                   buildConfig = true
               }

               configureKotlinAndroid(this)
           }
        }
    }
}