class Test {

    val end = 999999

    @Test
    fun testKonsole() {
        konsole.timeExecution {
            var i = 0
            while (i++ < end) {
                konsole.info("$i")
            }
        }
    }

}
