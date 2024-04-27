package libetal.libraries.kotlin.compiler.plugins.konsole

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.*
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CommandLineProcessor::class)
class KonsoleCommandLineProcessor : CommandLineProcessor {

    override val pluginId: String
        get() = "libetal.libraries.kotlin.compiler.plugins.konsole"

    override val pluginOptions: Collection<AbstractCliOption>
        get() = listOf(ENABLED_OPTION)

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) =
        when (option) {
            ENABLED_OPTION -> configuration.put(ENABLE_OPTION_KEY, value == "true")
            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
        }

    companion object {

        private const val ENABLED_OPTION_DESCRIPTION = "Allow using of the plugin"

        val ENABLED_OPTION = CliOption(
            "enabled",
            "true/false",
            ENABLED_OPTION_DESCRIPTION,
            required = false,
            allowMultipleOccurrences = false,
        )

        val ENABLE_OPTION_KEY: CompilerConfigurationKey<Boolean> =
            CompilerConfigurationKey.create(ENABLED_OPTION_DESCRIPTION)

    }
}
