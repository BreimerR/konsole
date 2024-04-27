package libetal.libraries.kotlin.compiler.plugins.konsole.fir

import org.jetbrains.kotlin.ir.expressions.IrExpression

inline fun <reified IR> IrExpression.visitExpression(transformer: (IR) -> IrExpression): IrExpression? =
    when (this) {
        is IR -> transformer(this)
        else -> null
    }
