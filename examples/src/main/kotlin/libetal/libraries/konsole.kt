package libetal.libraries

@Suppress("ClassName")
object konsole {

    fun info(message: String) {
        TODO("This Code should be converted to println($message). Ensure you implement plugin to use")
    }

    fun warn(message: String) {
        TODO("This Code should be converted to println($message). Ensure you implement plugin to use")

    }

    fun infoWithLineAndCol(tag: String, line: Int, col: Int, message: String) {
        println("[$tag]($line:$col)$message")
    }

    fun infoWithLineAndColAndDate(tag: String, line: Int, col: Int, message: String) {
        println("[$tag]($line:$col)$message")
    }

}
