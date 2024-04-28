package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.jvm.ir.receiverAndArgs
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.impl.IrGetObjectValueImpl
import org.jetbrains.kotlin.ir.util.packageFqName


val IrCall.fullQualifiedName: String?
    get() {
        return when (val context = receiverAndArgs().firstOrNull()) {
            is IrGetObjectValueImpl -> {
                val pkg = context.symbol.owner.name.asString()
                val pkgName = context.symbol.owner.packageFqName?.asString() ?: ""
                "$pkgName.$pkg"
            }

            else -> null
        }
    }
