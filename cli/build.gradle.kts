plugins {
    kotlin("jvm")
    kotlin("kapt")
    //alias(libs.plugins.konsole)
    id("libetal-gradle-versioner")
    `maven-publish`
}

val cliProjectGroup: String by extra

group = "$cliProjectGroup.compiler"
version = versioner.version

val artifactoryUrl: String by extra

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.konsole.jvm)
    compileOnly(libs.kotlin.compiler)

    kapt(libs.auto.service)
    compileOnly(libs.auto.service.annotations)


    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.3.6")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

publishing {
    publications {
        create<MavenPublication>("embeddableMaven") {
            artifactId = project.name
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
                username = System.getenv("MAVEN_USER_NAME").toString()
                password = System.getenv("MAVEN_PASSWORD").toString()
            }
            isAllowInsecureProtocol = true

        }

    }
}
