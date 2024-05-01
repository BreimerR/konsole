package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.jvm.ir.receiverAndArgs
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrFileEntry
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.impl.IrGetObjectValueImpl
import org.jetbrains.kotlin.ir.util.packageFqName


val IrCall.packageName: String?
    get() {
        return when (val context = receiverAndArgs().firstOrNull()) {
            is IrGetObjectValueImpl -> {
                val pkgName = context.symbol.owner.packageFqName?.asString() ?: ""
                val receiverName = receiverSimpleName?.let { ".$it" } ?: ""
                "$pkgName$receiverName"
            }

            else -> null
        }
    }

val IrCall.fqName: String?
    get() = packageName?.let { "$it.${symbol.owner.name}" }

val IrCall.simpleName
    get() = symbol.owner.name.asString()

val IrCall.receiverSimpleName: String?
    get() {
        return when (val context = receiverAndArgs().firstOrNull()) {
            is IrGetObjectValueImpl -> context.symbol.owner.name.asString()

            else -> null
        }
    }

fun IrElement.line(fileEntry: IrFileEntry) = fileEntry.getLineNumber(startOffset) + 1
fun IrElement.col(fileEntry: IrFileEntry) = fileEntry.getColumnNumber(startOffset) + 1

data class LocationDetails(val line: Int, val collection: Int)

fun IrElement.getLocationDetails(fileEntry: IrFileEntry) = LocationDetails(
    line(fileEntry),
    col(fileEntry)
)
