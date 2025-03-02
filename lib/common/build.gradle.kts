import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

plugins {
    kotlin("multiplatform")
    id("libetal-gradle-versioner")
    alias(libs.plugins.android.library)
    `maven-publish`
}

val projectGroup: String by extra

group = projectGroup
version = versioner.version

val artifactoryUrl: String by extra

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
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

    val hostOs = System.getProperty("os.name").trim().lowercase()
    val hostArch = System.getProperty("os.arch").trim().lowercase()

    val nativeTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> KotlinTarget =
        when (hostOs to hostArch) {
            "linux" to "aarch64" -> ::linuxArm64
            "linux" to "amd64" -> ::linuxX64
            "mac os x" to "aarch64" -> ::macosArm64
            "mac os x" to "amd64", "mac os x" to "x86_64" -> ::macosX64
            "windows 10" to "amd64", "windows server 2022" to "amd64" -> ::mingwX64
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
                implementation(kotlin("stdlib", libs.versions.kotlinVersion.get()))
                implementation(libs.kotlinx.datetime)
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
        create<MavenPublication>("konsoleSL4JMaven") {
            artifactId = project.name
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
                username = System.getenv("MAVEN_USER_NAME").toString()
                password = System.getenv("MAVEN_PASSWORD").toString()
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
