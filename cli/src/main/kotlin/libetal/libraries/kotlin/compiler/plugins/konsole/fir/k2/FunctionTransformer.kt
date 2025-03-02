package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.codegen.IrExpressionLambda
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrLoop
import org.jetbrains.kotlin.ir.expressions.IrWhen
import org.jetbrains.kotlin.ir.util.fileEntry
import org.jetbrains.kotlin.ir.util.kotlinFqName

class FunctionTransformer(
    private val irBuiltIns: IrBuiltIns,
    builder: DeclarationIrBuilder
) : IrElementTransformerWithBuilder<IrFunction>(builder) {

    override fun visitExpression(expression: IrExpression, data: IrFunction): IrExpression {
        val expressionTransformer = ExpressionTransformer(irBuiltIns, builder, data)

        return when (expression) {
            is IrWhen -> expression.transform(WhenBranchTransformer(builder, expressionTransformer), expression)

            is IrCall -> expression.transform(expressionTransformer, expression)

            is IrLoop -> {
                if (data.kotlinFqName.toString().contains("libetal.libraries.kui.engines.Vulkan")) {
                    println("konsole: ${data.kotlinFqName} ${expression.line(data.fileEntry)}")
                }
                expression
            }

            is IrExpressionLambda -> throw RuntimeException("Not supported yet")

            else -> expression
        }

    }
}
