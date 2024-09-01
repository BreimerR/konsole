package libetal.libraries

import kotlinx.datetime.*
import kotlinx.datetime.format.char
import libetal.kotlin.log.magenta

fun infoWithLineAndCol(label: String, line: Number, col: Number, message: String) {
    println("${getTag(label, line, col)}: $message")
}

fun infoWithLineAndColAndDate(tag: String, line: Long, col: Long, message: String) {
    val fullTag = getTag(tag, line, col)
    println("$nowAsString $fullTag:$message")
}

fun debug(filePath: String, label: String, line: Number, col: Number, message: String) {
    val coloredMessage = message.magenta
    val coloredLabel = label.magenta
    println("$nowAsString: $filePath:$line:$col [$coloredLabel]: $coloredMessage")
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
