package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrFileEntry
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrTypeAlias
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.fileEntry
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.FqName

class ExpressionTransformer(
    val moduleFragment: IrModuleFragment,
    builder: DeclarationIrBuilder,
    private val function: IrFunction
) : IrElementTransformerWithBuilder<IrExpression>(builder) {

    private fun IrBuilderWithScope.irKLog(
        expression: IrCall,
        fileEntry: IrFileEntry
    ) = when (expression.packageName) {
        "libetal.libraries.konsole" -> when (val message = expression.valueArguments.firstOrNull()) {
            null -> expression
            else -> when (val tag = expression.simpleName) {
                "info", "warn" -> expression.generateTaggedLogExpression(fileEntry, tag, message)
                "debug" -> expression.generateDebugExpression(
                    this,
                    fileEntry,
                    tag,
                    message
                )

                "timeExecution" ->
                    expression.transform(LambdaExpressionTransformer(this@ExpressionTransformer), expression)

                else -> expression
            }
        }

        else -> expression

    }

    private fun IrCall.generateEmpty() = irCall(printFunction, irString(""))

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

    override fun visitExpression(expression: IrExpression, data: IrExpression): IrExpression {
        return expression.visitExpression<IrCall> {
            builder.irKLog(it, function.fileEntry)
        } ?: super.visitExpression(expression, data)
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

    fun IrSimpleFunctionSymbol?.generateExpression(
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

    private fun IrBuilderWithScope.generateFailSafePrintLn(message: IrExpression): IrFunctionAccessExpression {
        return irCall(printLnFunction).also { call ->
            call.putValueArgument(0, message)
        }
    }

    private fun IrBuilderWithScope.irDebug(expression: IrExpression, function: IrFunction) {
        when (val body = function.body) {
            null -> return
            else -> function.body = builder.irBlockBody {
                when (expression) {
                    is IrCall -> {
                        val concat = builder.irConcat()
                        concat.addArgument(irString("Calling: ${expression.fqName} "))
                        +irCall(printLnFunction).also { call ->
                            call.putValueArgument(0, concat)
                        }
                    }
                }
                for (statement in body.statements) +statement
            }
        }
    }

    fun getPackageNameFromTypeAlias(typeAlias: IrTypeAlias): String? {
        val originalType = typeAlias.expandedType

        return originalType.classFqName?.asString()
    }


    private val printLnFunction by lazy {
        val functions = moduleFragment.irBuiltins.findFunctions(
            org.jetbrains.kotlin.name.Name.identifier("println"),
            FqName("kotlin.io")
        )

        functions.first {
            it.owner.valueParameters.isNotEmpty()
        }

    }

    private val printFunction by lazy {
        val functions = moduleFragment.irBuiltins.findFunctions(
            org.jetbrains.kotlin.name.Name.identifier("print"),
            FqName("kotlin.io")
        )

        functions.first {
            it.owner.valueParameters.isNotEmpty()
        }

    }

    private val infoWithLineAndCol by lazy {
        val functions = moduleFragment.irBuiltins.findFunctions(
            org.jetbrains.kotlin.name.Name.identifier("infoWithLineAndCol"),
            FqName("libetal.libraries")
        )

        functions.firstOrNull {
            true
        }

    }

    private val debugFunctionSymbol by lazy {
        val functions = moduleFragment.irBuiltins.findFunctions(
            org.jetbrains.kotlin.name.Name.identifier("debug"),
            FqName("libetal.libraries")
        )

        functions.firstOrNull {
            true
        }

    }

}
