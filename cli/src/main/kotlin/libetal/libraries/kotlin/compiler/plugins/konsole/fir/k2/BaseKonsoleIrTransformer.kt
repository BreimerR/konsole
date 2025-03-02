package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

abstract class BaseKonsoleIrTransformer<T : IrExpression>(protected val builder: DeclarationIrBuilder) :
    IrElementTransformerVoid() {

    abstract val IrExpression.transformElement: T?

    fun irCall(irFunction: IrFunction) = builder.irCall(irFunction)

    fun irCall(irFunction: IrFunctionSymbol) = builder.irCall(irFunction)

    fun irCall(irFunction: IrFunctionSymbol, vararg arguments: IrExpression) = builder.irCall(irFunction).apply {
        for ((index, argument) in arguments.withIndex()) {
            putValueArgument(index, argument)
        }
    }

    fun irConcat(vararg strings: IrExpression) = builder.irConcat().apply {
        for (string in strings) {
            addArgument(string)
        }
    }

    fun irConcat(vararg strings: String) = builder.irConcat().apply {
        for (string in strings) {
            addArgument(irString(string))
        }
    }

    fun irString(string: String) = builder.irString(string)

    fun IrCall.addArguments(vararg argument: IrExpression) {
        for ((i, arg) in argument.withIndex()) {
            putValueArgument(i, arg)
        }
    }

    override fun visitExpression(expression: IrExpression): IrExpression =
        super.visitExpression(expression.transformElement?.let(::transform) ?: expression)

    abstract fun transform(expression: T): T

}

context(IrStatementsBuilder<*>)
operator fun IrStatement?.unaryPlus() {
    val statement = this ?: return
    with(this@IrStatementsBuilder) {
        +statement
    }
}
