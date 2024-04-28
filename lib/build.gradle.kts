import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly

plugins {
    kotlin("multiplatform")

    `maven-publish`
}

val projectGroup: String by extra

group = projectGroup
version = libs.versions.konsoleVersion.get()

val artifactoryUrl: String by extra

kotlin {
//    jvmToolchain(17)
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

    when (hostOs) {

        "linux" -> linuxX64("native"){
            binaries{
                staticLib()
            }
        }

        "mac os x" -> when (hostArch) {
            "x86_64", "amd64" -> macosX64("native") {
                binaries {
                    sharedLib()
                    staticLib()
                }
            }

            "aarch64" -> macosArm64("native") {
                binaries {
                    sharedLib()
                    staticLib()
                }
            }
        }

        "windows 10", "windows server 2022" -> when (hostArch) {
            "amd64" -> mingwX64("native") {
                binaries {
                    sharedLib()
                    staticLib()
                }
            }
        }

        else -> throw GradleException("Host OS '$hostOs' with arch '$hostArch' is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0-RC1")
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
