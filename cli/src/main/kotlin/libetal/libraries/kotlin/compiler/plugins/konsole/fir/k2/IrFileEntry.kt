package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.ir.IrFileEntry

val IrFileEntry.filePath: String
    get() = "file://$name"
