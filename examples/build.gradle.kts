plugins {
    kotlin("jvm")
    alias(libs.plugins.konsole)
}

val artifactoryUrl: String by extra
group = "libetal.libraries.kotlin.compiler.plugins.konsole"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
