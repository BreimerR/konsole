package libetal.libraries.kotlin.compiler.plugins.konsole.fir

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer

class LogExpressionTransformer(val builder: DeclarationIrBuilder) : IrElementTransformer<IrExpression> {

    override fun visitExpression(expression: IrExpression, data: IrExpression): IrExpression {
        return when {
            expression.shouldTransform -> expression
            else -> super.visitExpression(expression, data)
        }
    }

    val IrExpression.shouldTransform
        get(): Boolean = true

}
