package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.InternalSymbolFinderAPI
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.IrFileEntry
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.fileEntry
import org.jetbrains.kotlin.name.FqName

class KonsoleExpressionTransformer(
    irBuiltIns: IrBuiltIns,
    builder: DeclarationIrBuilder,
    private val function: IrFunction
) : BaseKonsoleIrTransformer<IrCall>(builder) {

    @OptIn(InternalSymbolFinderAPI::class)
    private val debugFunctionSymbol by lazy {
        val functions = irBuiltIns.symbolFinder.findFunctions(
            org.jetbrains.kotlin.name.Name.identifier("debug"),
            FqName("libetal.libraries")
        )

        functions.firstOrNull {
            true
        }
    }

    @OptIn(InternalSymbolFinderAPI::class, UnsafeDuringIrConstructionAPI::class)
    private val printLnFunction: IrSimpleFunctionSymbol by lazy {
        val functions = irBuiltIns.symbolFinder.findFunctions(
            org.jetbrains.kotlin.name.Name.identifier("println"),
            FqName("kotlin.io")
        )

        functions.first {
            it.owner.valueParameters.isNotEmpty()
        }
    }

    private fun IrBuilderWithScope.irKLog(expression: IrCall, fileEntry: IrFileEntry) =
        when (val message = expression.valueArguments.firstOrNull()) {
            null -> expression
            else -> when (val tag = expression.simpleName) {
                "info", "warn" -> expression.generateTaggedLogExpression(fileEntry, tag, message)
                "debug" -> expression.generateDebugExpression(
                    this,
                    fileEntry,
                    tag,
                    message
                )

                "timeExecution" -> TODO("")

                else -> expression
            }
        }

    private fun IrCall.generateTaggedLogExpression(
        fileEntry: IrFileEntry,
        tag: String,
        message: IrExpression
    ): IrFunctionAccessExpression {

        val (line, col) = getLocationDetails(fileEntry)

        val messagePrefix = """($line:$col)[${tag.uppercase()}] : """

        val concat = irConcat(irString(messagePrefix), message)

        return irCall(printLnFunction, concat)

    }

    private fun IrCall.generateDebugExpression(
        scope: IrBuilderWithScope,
        fileEntry: IrFileEntry,
        tag: String,
        message: IrExpression
    ): IrFunctionAccessExpression = with(scope) {

        val (line, col) = getLocationDetails(fileEntry)

        return debugFunctionSymbol.generateExpression(message) { call ->
            call.addArguments(
                irString(fileEntry.filePath),
                irString(tag.uppercase()),
                irInt(line),
                irInt(col),
                message
            )
        }

    }

    private fun IrBuilderWithScope.generateFailSafePrintLn(message: IrExpression): IrFunctionAccessExpression {
        return irCall(printLnFunction).also { call ->
            call.putValueArgument(0, message)
        }
    }

    private fun IrSimpleFunctionSymbol?.generateExpression(
        message: IrExpression,
        requiredExpressionBuilder: (IrCall) -> Unit
    ): IrFunctionAccessExpression {
        return when (val function = this) {
            null -> builder.generateFailSafePrintLn(message)

            else -> builder.irCall(function).also {
                requiredExpressionBuilder(it)
            }
        }
    }

    override val IrExpression.transformElement: IrCall?
        get() = when {
            this is IrCall && fqName.toString().contains("libetal.libraries.konsole") -> this
            else -> null
        }

    override fun transform(expression: IrCall): IrCall = builder.irKLog(expression, function.fileEntry) as IrCall

}
