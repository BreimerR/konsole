plugins {
    kotlin("jvm")
    id("libetal-gradle-versioner")
    `maven-publish`
}

val projectGroup: String by extra

group = projectGroup
version = versioner.version

val artifactoryUrl: String by extra

dependencies {
    implementation(project(":konsole"))
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    api(libs.kotlinx.datetime)
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
