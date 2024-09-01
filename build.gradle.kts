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
    val sonarTypeUrl = "http://libetal.artifactory.com:8081/repository/kotlin-gradle-plugins/"
    val artifactoryUrl = "http://libetal.artifactory.com:8082/artifactory"
    extra["gradleArtifactoryUrl"] = "$artifactoryUrl/kotlin-gradle-plugins"
    extra["artifactoryUrl"] = "$artifactoryUrl/libetal"
    val projectGroup = "libraries.kotlin"
    extra["projectGroup"] = "libetal.$projectGroup"
    extra["cliProjectGroup"] =  "libetal.$projectGroup"

    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven {
            name = "localPluginRepository"
            url = uri(artifactoryUrl)
            credentials {
                username = System.getenv("MAVEN_USER_NAME").toString()
                password = System.getenv("MAVEN_PASSWORD").toString()
            }
            isAllowInsecureProtocol = true
        }
    }

}
