package libetal.libraries.kotlin.compiler.plugins.konsole

import com.google.auto.service.AutoService
import libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2.KonsoleLoweringExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CompilerPluginRegistrar::class)
class KonsoleComponentRegistrar : CompilerPluginRegistrar() {

    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val enabled = configuration.get(KonsoleCommandLineProcessor["enabled"].key, true)
        val enableWTF = configuration.get(KonsoleCommandLineProcessor["enableWTF"].key, enabled)
        val enableInfo = configuration.get(KonsoleCommandLineProcessor["enableInfo"].key, enabled)
        val enableDebug = configuration.get(KonsoleCommandLineProcessor["enableDebug"].key, enabled)
        val enableWarning = configuration.get(KonsoleCommandLineProcessor["enableWarning"].key, enabled)

        val konsoleConfigs = KonsoleConfigs(
            enabled = enabled,
            enableWTF = enableWTF,
            enableInfo = enableInfo,
            enableDebug = enableDebug,
            enableWarning = enableWarning
        )

        // We need to do a disabling Lowering pass first then do the Actual Lowering to avoid passing the
        // lowering configs down  stream
        // Use function.body = newFunction.body that removes the statement you do not need i.e konsole.info or konsole.log....

        IrGenerationExtension.registerExtension(KonsoleLoweringExtension(konsoleConfigs))

    }
}


data class KonsoleConfigs(
    val enabled: Boolean,
    val enableWTF: Boolean,
    val enableInfo: Boolean,
    val enableDebug: Boolean,
    val enableWarning: Boolean
)
