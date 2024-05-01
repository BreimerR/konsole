import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.konsole)
    // id("module.publication")
}



kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    val nativeTargetConfig: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit = {
        binaries {
            sharedLib()
            staticLib()
            // Remove if it is not executable
            "main".let { executable ->
                executable {
                    entryPoint = executable
                }
            }
        }
    }

    val hostOs = System.getProperty("os.name").trim().toLowerCaseAsciiOnly()
    val hostArch = System.getProperty("os.arch").trim().toLowerCaseAsciiOnly()

    when (hostOs to hostArch) {
        "linux" to "aarch64" -> linuxArm64("linuxArm64", nativeTargetConfig)
        "linux" to "amd64" -> linuxX64("linuxX64", nativeTargetConfig)
        "mac os x" to "aarch64" -> macosArm64("macosArm64", nativeTargetConfig)
        "mac os x" to "amd64", "mac os x" to "x86_64" -> macosX64("macosX64", nativeTargetConfig)
        "windows 10" to "amd64", "windows server 2022" to "amd64" -> mingwX64("mingwX64", nativeTargetConfig)

        else -> throw GradleException("Host OS '$hostOs' with arch '$hostArch' is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //implementation("libetal.libraries:konsole:${libs.versions.konsoleVersion.get()}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:+") // Replace with desired version
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-api:1.8.0-beta4")
                implementation("ch.qos.logback:logback-classic:+")
            }

        }
    }
}


android {
    namespace = "org.jetbrains.kotlinx.multiplatform.library.template"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}


konsole {
    sl4j {
        version = libs.versions.konsoleVersion.get()
    }

}
