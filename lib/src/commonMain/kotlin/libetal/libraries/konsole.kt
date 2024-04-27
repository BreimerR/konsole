package libetal.libraries

import kotlinx.datetime.*
import kotlinx.datetime.format.char


@Suppress("ClassName")
object konsole {

    fun info(message: String, vararg args: Any) {
        TODO("This Code should be converted to println($message). Ensure you implement plugin to use")
    }

    fun warn(message: String, vararg args: Any) {
        TODO("This Code should be converted to println($message). Ensure you implement plugin to use")
    }

    fun wtf(message: String, vararg args: Any) {
        TODO("This Code should be converted to println($message). Ensure you implement plugin to use")
    }

    fun error(message: String, vararg args: Any) {
        TODO("This Code should be converted to println($message). Ensure you implement plugin to use")
    }

    fun debug(message: String, vararg args: Any) {
        TODO("This Code should be converted to println($message). Ensure you implement plugin to use")
    }

    fun infoWithLineAndCol(tag: String, line: Long, col: Long, message: String) {
        println("${getTag(tag, line, col)}: $message")
    }

    fun infoWithLineAndColAndDate(tag: String, line: Long, col: Long, message: String) {
        val fullTag = getTag(tag, line, col)
        println("$now $fullTag:$message")
    }

    private fun getTag(tag: String, line: Long, col: Long) = "($line:$col)[$tag]"

    private val now: String
        get() {
            val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val format = LocalDateTime.Format {
                year()
                char('-')
                monthNumber()
                char('-')
                dayOfMonth()
                char(' ')
                hour()
                char(':')
                minute()
                char(':')
                second()
            }

            val formattedDateTime = currentDateTime.format(format)
            return formattedDateTime
        }

}
