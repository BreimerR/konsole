package libetal.libraries.kotlin.compiler.gradle

import libetal.libraries.kotlin.compiler.BuildConfig
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.*

class KonsoleGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun apply(target: Project) {
        if (target.name.startsWith("common")) {
            target.dependencies.add(
                "${BuildConfig.KONSOLE_GROUP_NAME}:${BuildConfig.KONSOLE_LIB_NAME}:${BuildConfig.KONSOLE_LIB_VERSION}",
                "kotlin"
            )
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

    override fun getPluginArtifactForNative() =
        SubpluginArtifact(
            BuildConfig.KONSOLE_GROUP_NAME,
            BuildConfig.KONSOLE_PLUGIN_NAME,
            BuildConfig.KONSOLE_PLUGIN_VERSION
        )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

}
