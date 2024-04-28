import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.android.library)
    `maven-publish`
}

val projectGroup: String by extra

group = projectGroup
version = libs.versions.konsoleVersion.get()

val artifactoryUrl: String by extra

kotlin {
//    jvmToolchain(17)

    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
//    js(IR) {
//        browser()
//        nodejs()
//        binaries.library()
//    }
    val hostOs = System.getProperty("os.name").trim().toLowerCaseAsciiOnly()
    val hostArch = System.getProperty("os.arch").trim().toLowerCaseAsciiOnly()

    val nativeTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> KotlinTarget =
        when (hostOs to hostArch) {
            "linux" to "aarch64" -> ::linuxArm64
            "linux" to "amd64" -> ::linuxX64
//            "linux" to "arm", "linux" to "arm32" -> ::linuxArm32Hfp
//            "linux" to "mips", "linux" to "mips32" -> ::linuxMips32
//            "linux" to "mipsel", "linux" to "mips32el" -> ::linuxMipsel32
            "mac os x" to "aarch64" -> ::macosArm64
            "mac os x" to "amd64", "mac os x" to "x86_64" -> ::macosX64
            "windows 10" to "amd64", "windows server 2022" to "amd64" -> ::mingwX64
//            "windows" to "x86" -> ::mingwX86
            else -> throw GradleException("Host OS '$hostOs' with arch '$hostArch' is not supported in Kotlin/Native.")
        }
    nativeTarget("native") {
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

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib", "2.0.0-RC1"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:+")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }

}

publishing {
    publications {
        create<MavenPublication>("konsoleMaven") {
            artifactId = project.name
            version = libs.versions.konsoleVersion.get()
            from(components["kotlin"])
            pom {
                name = "konsole"
                developers {
                    developer {
                        id = "breimer"
                        name = "Breimer Radido"
                        email = "breimer@gmail.com"
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "artifactoryPublication"
            url = uri(artifactoryUrl)
            credentials {
                username = "admin"
                password = "H.v86j^Xcf"
            }
            isAllowInsecureProtocol = true

        }

    }
}


android {
    namespace = projectGroup
    compileSdk = libs.versions.android.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.min.sdk.get().toInt()
    }
}
