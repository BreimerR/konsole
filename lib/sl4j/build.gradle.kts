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
    implementation("org.slf4j:slf4j-api:1.8.0-beta4")
    implementation("ch.qos.logback:logback-classic:1.5.8")
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
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
