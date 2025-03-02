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

abstract class KonsoleIrExpressionTransformerVoid<T : IrExpression>(protected val builder: DeclarationIrBuilder) :
    IrElementTransformerVoid() {

    abstract val T.shouldTransform: Boolean

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

    override fun visitFunction(declaration: IrFunction): IrStatement {
        declaration.body = declaration.body?.let {
            when (it) {
                is IrBlockBody -> visitBlockBody(it)
                else -> it
            }
        }
        return super.visitFunction(declaration)
    }

    override fun visitBody(body: IrBody): IrBody {
        val newBody = builder.irBlockBody {
            for (statement in body.statements) {
                val newStatement = when (statement) {
                    is IrExpression -> when (val statementTransformElement = statement.transformElement) {
                        null -> null
                        else -> when (statementTransformElement.shouldTransform) {
                            true -> transform(statementTransformElement)
                            else -> statement
                        }
                    }

                    else -> statement
                }

                +newStatement
            }
        }
        return super.visitBlockBody(newBody)
    }

    override fun visitExpression(expression: IrExpression): IrExpression {
        val newExpression = when (val expressionTargetElement = expression.transformElement) {
            null -> expression
            else -> when (expressionTargetElement.shouldTransform) {
                true -> transform(expression, expressionTargetElement)
                else -> when (expressionTargetElement) {
                    is IrCall -> {
                        for ((i, argument) in expressionTargetElement.valueArguments.withIndex()) {
                            if (argument is IrFunctionExpression) {

                                argument.function = argument.function.transform(this, null) as IrSimpleFunction

                                expressionTargetElement.putValueArgument(i, argument)
                            }
                        }
                        expressionTargetElement
                    }

                    else -> expressionTargetElement
                }
            }
        }
        return super.visitExpression(newExpression)

    }

    abstract fun transform(expression: T): T?

    private fun transform(default: IrExpression, expression: T) = transform(expression) ?: default

}
