package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2


import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrWhen

class WhenBranchTransformer(builder: DeclarationIrBuilder, private val expressionTransformer: ExpressionTransformer) :
    IrElementTransformerWithBuilder<IrWhen>(builder) {

    override fun visitExpression(expression: IrExpression, data: IrWhen): IrExpression {
        val newExpression = when (expression) {
            is IrCall -> expression.transform(expressionTransformer, data)
            else -> expression
        }
        return super.visitExpression(newExpression, data)
    }

}
