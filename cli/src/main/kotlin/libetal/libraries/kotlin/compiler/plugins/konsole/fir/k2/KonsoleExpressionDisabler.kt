package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression

class KonsoleExpressionDisabler(builder: DeclarationIrBuilder, val validator: IrCall.() -> Boolean) :
    KonsoleIrExpressionTransformerVoid<IrCall>(builder) {

    override val IrCall.shouldTransform: Boolean
        get() = validator()

    override val IrExpression.transformElement: IrCall?
        get() = when (this) {
            is IrCall -> this
            else -> null
        }

    override fun transform(expression: IrCall): IrCall? = null

}
