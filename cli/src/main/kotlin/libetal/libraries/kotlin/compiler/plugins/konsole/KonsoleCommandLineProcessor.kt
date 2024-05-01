package libetal.libraries.kotlin.compiler.plugins.konsole

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.*
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CommandLineProcessor::class)
class KonsoleCommandLineProcessor : CommandLineProcessor {

    override val pluginId: String
        get() = "konsole-cli"


    override val pluginOptions: Collection<AbstractCliOption>
        get() = cliOptionDelegates.map { it.option }

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {

        val cliOptionDelegate = cliOptionDelegates.firstOrNull { it.name == option.optionName }

        when (cliOptionDelegate) {
            null -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
            else -> configuration.put(cliOptionDelegate.key, cliOptionDelegate.converter(value))
        }
    }

    companion object {

        val ENABLED_NAME = "enabled"
        val ENABLED_WTF = "enableWTF"
        val ENABLE_INFO = "enableInfo"
        val ENABLE_WARN = "enableWarning"
        val ENABLE_DEBUG = "enableDebug"

        private val cliOptionDelegates by lazy {
            listOf(
                CliOptionDelegate(
                    ENABLED_NAME,
                    "true/false",
                    "Allow using of the plugin"
                ) {
                    it.toBoolean()
                },
                CliOptionDelegate(
                    ENABLED_WTF,
                    "true/false",
                    "Enable WTF logs"
                ) {
                    it.toBoolean()
                },
                CliOptionDelegate(
                    ENABLE_INFO,
                    "true/false",
                    "Enable WTF logs"
                ) {
                    it.toBoolean()
                },
                CliOptionDelegate(
                    ENABLE_DEBUG,
                    "true/false",
                    "Enable WTF logs"
                ) {
                    it.toBoolean()
                },
                CliOptionDelegate(
                    ENABLE_WARN,
                    "true/false",
                    "Enable WTF logs"
                ) {
                    it.toBoolean()
                },
            )
        }

        operator fun get(name: String) = cliOptionDelegates.first { it.name == name }

        class CliOptionDelegate<T>(
            val name: String,
            valueDescription: String,
            description: String,
            required: Boolean = false,
            single: Boolean = true,
            val converter: (String) -> T
        ) {
            val option = CliOption(
                name,
                valueDescription,
                description,
                required = required,
                allowMultipleOccurrences = !single,
            )
            val key = CompilerConfigurationKey<T>(name)
        }

    }
}
