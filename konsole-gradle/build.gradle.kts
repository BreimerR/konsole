plugins {
    kotlin("jvm")
    id("com.github.gmazzo.buildconfig")
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.artifactory)
    `maven-publish`

}

val gradleArtifactoryUrl: String by extra
val artifactoryUrl: String by extra

val projectGroup: String by extra
group = projectGroup
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
            name = "artifactoryPluginPublication"
            url = uri(gradleArtifactoryUrl)
            credentials {
                username = "admin"
                password = "H.v86j^Xcf"
            }
            isAllowInsecureProtocol = true

        }

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

    publications {

        withType<MavenPublication> {
            println("Publication $name")
        }

        create<MavenPublication>("pluginMaven") {
            val isGradlePluginDependency = artifactId == project.name
            groupId = "$groupId.compiler"
            pom {
                name = if (isGradlePluginDependency) "${project.name}.gradle.plugin" else project.name
                setDescription(
                    if (isGradlePluginDependency)
                        "This is the gradle plugin resolver"
                    else
                        "This is the gradle plugin"
                )
            }
        }

    }

}

tasks.withType<PublishToMavenRepository> {
    val predicate = provider {
        (repository == publishing.repositories["artifactoryPublication"] && publication == publishing.publications["pluginMaven"]) ||
                (repository == publishing.repositories["artifactoryPluginPublication"] && publication == publishing.publications["konsolePluginMarkerMaven"])
    }
    onlyIf("publishing binary to the external repository, or binary and sources to the internal one") {
        predicate.get()
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
    buildConfigField("String", "KONSOLE_LIB_NAME", "\"${libProject.name}\"")
    buildConfigField("String", "KONSOLE_LIB_GROUP_NAME", """"${libProject.group}"""")

    val sl4jProject = project(":konsole-sl4j")
    buildConfigField(
        "String?",
        "KONSOLE_SL4J_VERSION",

        sl4jProject.project.version.toString().ifBlank { null }
            .let { if (it == "unspecified") null else it }
            .let { string ->
                when (string) {
                    null -> """"${libs.versions.konsoleVersion.get()}""""
                    else -> """"$string""""
                }
            }
    )

}
