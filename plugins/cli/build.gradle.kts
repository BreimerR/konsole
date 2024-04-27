plugins {
    kotlin("jvm")
    kotlin("kapt")
    `maven-publish`
}

group = "libetal.libraries.kotlin.compiler"
version = libs.versions.konsoleVersion.get()

val artifactoryUrl: String by extra

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":konsole-k2"))
    compileOnly("org.jetbrains.kotlin:kotlin-compiler")

    kapt("com.google.auto.service:auto-service:1.0.1")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0.1")


    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.3.6")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}


publishing {
    publications {
        create<MavenPublication>("embeddableMaven") {
            artifactId = project.name
            version = libs.versions.konsoleVersion.get()
            from(components["kotlin"])
            pom {
                name = project.name
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
