package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irConcat
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.addArgument
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer

abstract class IrElementTransformerWithBuilder<D>(val builder: DeclarationIrBuilder) : IrElementTransformer<D> {

    constructor(pluginContext: IrPluginContext, symbol: IrFunctionSymbol) : this(
        DeclarationIrBuilder(
            pluginContext,
            symbol
        )
    )

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

}
