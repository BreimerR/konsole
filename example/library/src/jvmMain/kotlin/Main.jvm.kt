import libetal.libraries.konsole
import org.slf4j.LoggerFactory

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object L

actual fun test() {
    val end = 99999999
    val job = CoroutineScope(Dispatchers.IO).launch {
        konsole.timeExecution {
            var i = 0
            while (i++ < end) {
                launch(Dispatchers.IO) {
                    konsole.info("")
                }

            }
        }
    }

}
