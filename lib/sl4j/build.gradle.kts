plugins {
    kotlin("jvm")
    `maven-publish`
}

val projectGroup: String by extra

group = projectGroup
version = libs.versions.konsoleVersion.get()

val artifactoryUrl: String by extra

dependencies {
    implementation(project(":konsole"))
    implementation("org.slf4j:slf4j-api:1.8.0-beta4")
    implementation("ch.qos.logback:logback-classic:+")
    api("org.jetbrains.kotlinx:kotlinx-datetime:+")
}

publishing {
    publications {
        create<MavenPublication>("konsoleSL4JMaven") {
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
