package libetal.libraries.kotlin.compiler.plugins.konsole.fir

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.js.translate.expression.ExpressionVisitor
import org.jetbrains.kotlin.js.translate.general.TranslatorVisitor


interface IrExpressionTransformer<D> : IrElementVisitor<IrExpression, D> {

    override fun visitElement(element: IrElement, data: D): IrExpression {
        return when (element) {
            is IrExpression -> visitExpression(element)
            else -> throw IllegalStateException("Unexpected element type ${element::class} expected expression")
        }
    }

    fun visitExpression(element: IrExpression): IrExpression
}
