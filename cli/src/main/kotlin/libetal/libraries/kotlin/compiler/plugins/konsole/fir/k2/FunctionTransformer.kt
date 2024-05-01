package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import libetal.libraries.kotlin.compiler.plugins.konsole.KonsoleConfigs
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrWhen
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol

class FunctionTransformer(
    private val moduleFragment: IrModuleFragment,
    builder: DeclarationIrBuilder
) : IrElementTransformerWithBuilder<IrFunction>(builder) {

    override fun visitExpression(expression: IrExpression, data: IrFunction): IrExpression {
        val expressionTransformer = ExpressionTransformer( moduleFragment, builder, data)

        return when (expression) {
            is IrWhen -> {
                val newBranches = mutableListOf<IrBranch>()
                for (branch in expression.branches) {
                    newBranches.add(branch.transform(WhenBranchTransformer(builder, expressionTransformer), branch))
                }
                expression.branches.clear()
                expression.branches.addAll(newBranches)
                expression
            }

            is IrCall -> expression.transform(expressionTransformer, expression)

            else -> expression
        }

    }

}
