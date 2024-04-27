plugins {
    kotlin("multiplatform") version libs.versions.kotlinVersion apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.gradle.publish) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.artifactory) apply false
    id("com.github.gmazzo.buildconfig") version "3.1.0" apply false

}


allprojects {
    val artifactoryUrl = "http://127.0.1.1:8082/artifactory/libetal"
    extra["artifactoryUrl"] = artifactoryUrl

    repositories {
        google()
        mavenLocal()
        mavenCentral()
        /*maven {
            name = "localPluginRepository"
            url = uri(artifactoryUrl)
            credentials {
                username = "admin"
                password = "H.v86j^Xcf"
            }
            isAllowInsecureProtocol = true
        }*/
    }

}
