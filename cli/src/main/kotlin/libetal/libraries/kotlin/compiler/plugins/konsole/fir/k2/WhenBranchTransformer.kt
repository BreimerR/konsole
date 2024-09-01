package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2


import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.util.fileEntry

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
