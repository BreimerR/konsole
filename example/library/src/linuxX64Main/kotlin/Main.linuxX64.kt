import libetal.libraries.konsole

actual fun test() {
    konsole.timeExecution {
        var i = 0
        while (i < end) {
            konsole.debug("Test")
            i++
        }
    }
}
