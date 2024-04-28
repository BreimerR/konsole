import libetal.libraries.konsole

class Main {
    fun main() {
        if (true) konsole.info("Hello World") else konsole.info("Hello World!")
    }

    fun lambdaTest() {
        konsole.timeExecution {
            konsole.info("Execution time")
        }
    }
}

fun main(vararg args: String) {
    val instance = Main()
    instance.main()
    instance.lambdaTest()

}

data class Smile(val name: String, val age: Int)
