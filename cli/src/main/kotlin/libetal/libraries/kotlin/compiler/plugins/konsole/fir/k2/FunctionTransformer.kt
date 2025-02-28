package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.common.serialization.proto.IrWhile
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.util.file
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

            is IrLoop -> expression.transform(expressionTransformer, expression)

            else -> expression
        }

    }
}
