package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression

class FunctionExpressionTransformer(
    private val expressionTransformer: ExpressionTransformer
) : IrElementTransformerWithBuilder<IrFunctionExpression>(expressionTransformer.builder) {

    override fun visitExpression(expression: IrExpression, data: IrFunctionExpression): IrExpression =
        expression.transform(expressionTransformer, expression)

}
