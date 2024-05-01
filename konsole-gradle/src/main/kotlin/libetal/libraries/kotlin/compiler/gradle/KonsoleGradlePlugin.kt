package libetal.libraries.kotlin.compiler.gradle


import libetal.libraries.kotlin.compiler.BuildConfig
import libetal.libraries.kotlin.compiler.gradle.extensions.KonsoleGradleExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.*

class KonsoleGradlePlugin : KotlinCompilerPluginSupportPlugin {

    val konsoleDependency
        get() = "${BuildConfig.KONSOLE_LIB_GROUP_NAME}:${BuildConfig.KONSOLE_LIB_NAME}:${BuildConfig.KONSOLE_LIB_VERSION}"

    private fun getConsoleDependency(
        extension: String? = null,
        version: String = BuildConfig.KONSOLE_LIB_VERSION
    ): String {
        val targetName = if (extension?.ifBlank { null } == null) "" else "-$extension"
        return "${BuildConfig.KONSOLE_LIB_GROUP_NAME}:${BuildConfig.KONSOLE_LIB_NAME}${targetName}:${version}"
    }

    override fun apply(target: Project) {

        target.extensions.create("konsole", KonsoleGradleExtension::class.java)

        super.apply(target)
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> =
        kotlinCompilation.target.project.let { project ->
            val konsoleExtension =
                kotlinCompilation.target.project.extensions.getByType(KonsoleGradleExtension::class.java)

            val cliArgs = mutableListOf<SubpluginOption>()

            kotlinCompilation.kotlinSourceSets.forEach {
                it.dependencies {
                    if (it.name.contains("commonMain")) implementation(getConsoleDependency())

                    if (it.name.contains("(javaMain|jvmMain)".toRegex())) {
                        when (val sl4j = konsoleExtension.sl4j) {
                            null -> implementation(getConsoleDependency("jvm"))
                            else -> {
                                cliArgs.add(
                                    SubpluginOption("sl4jEnabled", "true")
                                )
                                implementation(
                                    getConsoleDependency(
                                        "sl4j",
                                        sl4j.version ?: BuildConfig.KONSOLE_SL4J_VERSION
                                        ?: BuildConfig.KONSOLE_LIB_VERSION
                                    )
                                )
                            }
                        }
                    }
                }

            }

            cliArgs.add(SubpluginOption("enabled", konsoleExtension.enabled.toString()))
            cliArgs.add(SubpluginOption("enableWTF", konsoleExtension.enableWTF.toString()))
            cliArgs.add(SubpluginOption("enableInfo", konsoleExtension.enableInfo.toString()))
            cliArgs.add(SubpluginOption("enableDebug", konsoleExtension.enableDebug.toString()))
            cliArgs.add(SubpluginOption("enableWarning", konsoleExtension.enableWarning.toString()))

            project.provider { cliArgs }
        }

    override fun getCompilerPluginId(): String = BuildConfig.KONSOLE_PLUGIN_NAME

    override fun getPluginArtifact() =
        SubpluginArtifact(
            BuildConfig.KONSOLE_GROUP_NAME,
            BuildConfig.KONSOLE_PLUGIN_NAME,
            BuildConfig.KONSOLE_PLUGIN_VERSION
        )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

}
