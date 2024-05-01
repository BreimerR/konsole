package libetal.libraries


@Suppress("ClassName")
object konsole {

    private const val PLUGIN_INSTALLATION_ERROR = """Ensure you implement plugin id("konsole-plugin")"""

    fun info(message: String, vararg args: Any) {
        TODO("This Code should be converted to println($message). $PLUGIN_INSTALLATION_ERROR")
    }

    fun info(tag: String, message: String, vararg args: Any) {
        TODO("This Code should be converted to println($tag: $message). $PLUGIN_INSTALLATION_ERROR")
    }

    fun warn(message: String, vararg args: Any) {
        TODO("This Code should be converted to println($message). $PLUGIN_INSTALLATION_ERROR")
    }

    fun wtf(message: String, vararg args: Any) {
        TODO("This Code should be converted to println($message). $PLUGIN_INSTALLATION_ERROR")
    }

    fun error(message: String, vararg args: Any) {
        TODO("This Code should be converted to println($message). $PLUGIN_INSTALLATION_ERROR")
    }

    fun debug(message: String, vararg args: Any) {
        TODO("This Code should be converted to println($message). $PLUGIN_INSTALLATION_ERROR")
    }

    fun logStart() {
        TODO("""This code should be converted to println("$nowAsString"). $PLUGIN_INSTALLATION_ERROR""")
    }

    /**
     * Logs the duration a function took to execute.
     * There might be a couple so how do we
     *
     * import kotlinx.coroutines.currentCoroutineContext
     *
     * val coroutineName = currentCoroutineContext()[CoroutineName]?.name
     **/
    fun <T> timeExecution(expression: () -> T): T {
        val start = nowEpochSeconds
        val result = expression()
        val duration = nowEpochSeconds - start
        println("[INFO] TOOK ${duration}ms")
        return result
    }

}
