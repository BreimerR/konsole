pluginManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven {
            name = "localPluginRepository"
            url = uri("http://127.0.1.1:8082/artifactory")
            credentials {
                username = "admin"
                password = "H.v86j^Xcf"
            }
            isAllowInsecureProtocol = true
        }

        maven {
            url = uri("http://libetal.artifactory.com:8082/artifactory/kotlin-gradle-plugins/")
            isAllowInsecureProtocol = true
            credentials {
                username = System.getenv("MAVEN_USER_NAME").toString()
                password = System.getenv("MAVEN_PASSWORD").toString()
            }
        }

        maven {
            url = uri("http://libetal.artifactory.com:8081/repository/kotlin-gradle-plugins/")
            isAllowInsecureProtocol = true
            credentials {
                username = System.getenv("MAVEN_USER_NAME").toString()
                password = System.getenv("MAVEN_PASSWORD").toString()
            }
        }

    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "konsole"

include(":konsole-cli")
project(":konsole-cli").projectDir = file("$rootDir/cli")

//include(":konsole-k2")
//project(":konsole-k2").projectDir = file("$rootDir/plugins/k2")

include(":konsole-plugin")
project(":konsole-plugin").projectDir = file("$rootDir/konsole-gradle")

include(":konsole")
project(":konsole").projectDir = file("$rootDir/lib/common")

include(":konsole-sl4j")
project(":konsole-sl4j").projectDir = file("$rootDir/lib/sl4j")
