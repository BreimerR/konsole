import libetal.libraries.konsole

object L

actual fun test() {
    val end = 99999
    konsole.timeExecution {
        var i = 0
        while (i++ < end) {
            konsole.info("")
        }
    }

}
