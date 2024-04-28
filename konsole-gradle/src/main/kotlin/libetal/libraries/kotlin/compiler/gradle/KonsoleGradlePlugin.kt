package libetal.libraries.kotlin.compiler.gradle


import libetal.libraries.kotlin.compiler.BuildConfig
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.*

class KonsoleGradlePlugin : KotlinCompilerPluginSupportPlugin {

    val konsoleDependency
        get() = "${BuildConfig.KONSOLE_LIB_GROUP_NAME}:${BuildConfig.KONSOLE_LIB_NAME}:${BuildConfig.KONSOLE_LIB_VERSION}"


    private fun getConsoleDependency(target: String? = null): String {
        val targetName = if (target?.ifBlank { null } == null) "" else "-$target"
        return "${BuildConfig.KONSOLE_LIB_GROUP_NAME}:${BuildConfig.KONSOLE_LIB_NAME}${targetName}:${BuildConfig.KONSOLE_LIB_VERSION}"
    }

    override fun apply(target: Project) {

        for (configuration in target.configurations){
            configuration ?: continue

            val dependencyArtifact  = when {
                configuration.name == "commonMainApi" || configuration.name == "commonMainApiDependenciesMetadata" -> getConsoleDependency()

                configuration.name.contains("linux") ->   getConsoleDependency("native")

                configuration.name.contains("ios") -> getConsoleDependency("ios")

                configuration.name.contains("android") -> getConsoleDependency("android")

                else -> null
            }
            if (dependencyArtifact == null){
                //println("Configuration '${configuration.name}' can't receive $konsoleDependency working on a configurable but then again just adding $konsoleDependency works just fine 😅. This is just for the standard user")
                continue
            }

            configuration.dependencies.add(target.dependencies.create(dependencyArtifact.trim('.')))
        }





        super.apply(target)
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> =
        kotlinCompilation.target.project.provider { emptyList() }

    override fun getCompilerPluginId(): String = BuildConfig.KONSOLE_PLUGIN_NAME

    override fun getPluginArtifact() =
        SubpluginArtifact(
            BuildConfig.KONSOLE_GROUP_NAME,
            BuildConfig.KONSOLE_PLUGIN_NAME,
            BuildConfig.KONSOLE_PLUGIN_VERSION
        )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

}
