package libetal.kotlin.log

const val RED = "\u001b[31m"
const val BRIGHT_RED = "\u001b[91m"
const val BLACK = "\u001b[30m"
const val GREEN = "\u001b[32m"
const val YELLOW = "\u001b[33m"
const val BLUE = "\u001b[34m"
const val MAGENTA = "\u001b[35m"
const val CYAN = "\u001b[36m"
const val WHITE = "\u001b[37m"
const val BRIGHT_WHITE = "\u001b[97m"

const val UNDERLINE = "\u001b[21m"

const val BLACK_BACKGROUND = "\u001b[40m"
const val RED_BACKGROUND = "\u001b[41m"
const val GREEN_BACKGROUND = "\u001b[42m"
const val YELLOW_BACKGROUND = "\u001b[43m"
const val BLUE_BACKGROUND = "\u001b[44m"

const val RESET = "\u001b[0m"

val String.reset
    get() = this + RESET

val String.red
    get() = RED + reset

val String.brightRed
    get() = BRIGHT_RED + reset

val String.black
    get() = BLACK + reset


val String.green
    get() = GREEN + reset

val String.yellow
    get() = YELLOW + reset

val String.blue
    get() = BLUE + reset

val String.magenta
    get() = MAGENTA + reset

val String.cyan
    get() = CYAN + reset

val String.white
    get() = BRIGHT_WHITE + reset

val String.dullWhite
    get() = WHITE + reset

val String.underline
    get() = UNDERLINE + reset



