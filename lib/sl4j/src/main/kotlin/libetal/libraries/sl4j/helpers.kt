package libetal.libraries.sl4j

import kotlinx.datetime.*
import kotlinx.datetime.format.char


fun infoWithLineAndColAndDate(label: String, line: Long, col: Long, message: String) {
    val fullTag = getTag(label, line, col)
    println("$nowAsString $fullTag:$message")
}

fun debug(filePath: String, tag: String, line: Number, col: Number, message: String) {
    val fullTag = getTag(tag, line, col)
    println("$nowAsString: $filePath $fullTag: $message")
}

private fun getTag(label: String, line: Number, col: Number) = "($line:$col)[$label]"

internal val now
    get() = Clock.System.now()

internal val nowEpochSeconds
    get() = now.epochSeconds

internal val nowAsString: String
    get() {
        val currentDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
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
