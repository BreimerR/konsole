package libetal.libraries.kotlin.compiler.gradle




import libetal.libraries.kotlin.compiler.BuildConfig
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.*

class KonsoleGradlePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project) {
        target.dependencies.add(
            "implementation",
            "${BuildConfig.KONSOLE_LIB_GROUP_NAME}:${BuildConfig.KONSOLE_PLUGIN_NAME}:${BuildConfig.KONSOLE_PLUGIN_VERSION}"
        )
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
