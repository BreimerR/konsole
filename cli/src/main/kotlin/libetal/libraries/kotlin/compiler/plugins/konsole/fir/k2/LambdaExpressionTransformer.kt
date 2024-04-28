package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression

class LambdaExpressionTransformer(
    private val expressionTransformer: ExpressionTransformer
) : IrElementTransformerWithBuilder<IrFunctionAccessExpression>(expressionTransformer.builder) {

    override fun visitCall(expression: IrCall, data: IrFunctionAccessExpression): IrElement = expression.apply {
        for ((i, arg) in expression.valueArguments.withIndex()) {
            val transformed = when (arg) {
                is IrFunctionExpression -> arg.transform(FunctionExpressionTransformer(expressionTransformer), arg)
                else -> arg
            }
            putValueArgument(i, transformed)
        }
    }

}
