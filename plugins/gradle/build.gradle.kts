plugins {
    kotlin("jvm")
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.artifactory)
    `maven-publish`
    id("com.github.gmazzo.buildconfig")

}

val artifactoryUrl: String by extra
val publishingUrl = "http://127.0.1.1:8082/artifactory"

group = "libetal.libraries.kotlin.compiler"
version = libs.versions.konsoleVersion.get()

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:+")

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

gradlePlugin {

    plugins {
        create("konsole") {
            id = "konsole-plugin"
            displayName = "Kotlin Konsole plugin"
            description = displayName
            implementationClass = "libetal.libraries.kotlin.compiler.gradle.KonsoleGradlePlugin"
            tags = setOf("Kotlin", "Konsole")
        }
    }

}


publishing {
    repositories {
        maven {
            name = "artifactoryPublication"
            url = uri(publishingUrl) //http://127.0.1.1:8082/artifactory/konsole-plugin
            credentials {
                username = "admin"
                password = "H.v86j^Xcf"
            }
            isAllowInsecureProtocol = true

        }

    }

    publications {
        register<MavenPublication>("pluginMavenArtifactory") {
            from(components["kotlin"])
        }

        register<MavenPublication>("examplePluginMarkerMaven") {
            groupId =  project.name + ".gradle.plugin"
            artifactId = project.name + ".gradle.plugin"
            version = libs.versions.konsoleVersion.get()
            pom.withXml {
                asNode().appendNode("name", project.name)
                asNode().appendNode("description", project.description)
                val dependency = asNode().appendNode("dependencies").appendNode("dependency")
                dependency.appendNode("groupId", group.toString())
                dependency.appendNode("artifactId", rootProject.name)
                dependency.appendNode("version", version)
            }
        }
    }

}


buildConfig {
    val project = project(":konsole-cli")
    packageName(project.group.toString())
    buildConfigField("String", "KONSOLE_GROUP_NAME", "\"${project.group}\"")
    buildConfigField("String", "KONSOLE_PLUGIN_NAME", "\"${project.name}\"")
    buildConfigField("String", "KONSOLE_PLUGIN_VERSION", "\"${project.version}\"")

    val libProject = project(":konsole")
    buildConfigField("String", "KONSOLE_LIB_VERSION", "\"${libProject.version}\"")
    buildConfigField("String", "KONSOLE_LIB_NAME", "\"${libProject.version}\"")
    buildConfigField("String", "KONSOLE_LIB_GROUP_NAME", "\"${libProject.group}\"")

}
