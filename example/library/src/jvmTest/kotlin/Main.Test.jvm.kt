import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import libetal.libraries.konsole
import org.junit.Test
import org.slf4j.LoggerFactory


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

    @Test
    fun testSL4j() {
        konsole.timeExecution {
            val logger = LoggerFactory.getLogger("Test")
            var i = 0
            while (i++ < end) {
                logger.info("$i")
            }
        }
    }

}
