package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrWhen
import org.jetbrains.kotlin.ir.util.kotlinFqName

class FunctionTransformer(
    private val moduleFragment: IrModuleFragment,
    builder: DeclarationIrBuilder
) : IrElementTransformerWithBuilder<IrFunction>(builder) {

    override fun visitExpression(expression: IrExpression, data: IrFunction): IrExpression {
        val expressionTransformer = ExpressionTransformer(moduleFragment, builder, data)

        return when ( expression) {
            is IrWhen -> if("${data.kotlinFqName}" == "libetal.libraries.kui.application.glfwApplication")
                expression.transform(WhenBranchTransformer(builder, expressionTransformer), expression)
            else expression

            is IrCall -> expression.transform(expressionTransformer, expression)

            else -> expression
        }

    }

}
