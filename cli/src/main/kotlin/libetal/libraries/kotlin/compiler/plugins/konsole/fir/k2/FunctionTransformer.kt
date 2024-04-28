package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrTypeAlias
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.name.FqName

class FunctionTransformer(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext, symbol: IrFunctionSymbol) :
    IrElementTransformerWithBuilder<IrFunction>(pluginContext, symbol) {

    val IrExpression.shouldTransform
        get(): Boolean = true

    private val typeAnyNullable by lazy {
        pluginContext.irBuiltIns.anyNType
    }

    val logFunction by lazy {
        val functions = moduleFragment.irBuiltins.findFunctions(
            org.jetbrains.kotlin.name.Name.identifier("println"),
            FqName("kotlin.io")
        )

        functions.first {
            it.owner.valueParameters.isNotEmpty()
        }
    }

    override fun visitExpression(expression: IrExpression, data: IrFunction): IrExpression {
        return expression.visitExpression<IrCall> {
            builder.irKLog(it)
        } ?: super.visitExpression(expression, data)
    }

    private fun IrBuilderWithScope.irKLog(
        expression: IrCall
    ): IrFunctionAccessExpression = when (val symbolOwner = expression.fullQualifiedName) {
        "libetal.libraries.konsole" -> {
            val arguments = expression.valueArguments.firstOrNull()
            val concat = irConcat()
            // concat.addArgument(irString(arguments))
            irCall(logFunction).also { call ->
                arguments?.let {
                    call.putValueArgument(0, arguments)
                }
            }
        }

        else -> {
            val arguments = expression.valueArguments.firstOrNull()
            val concat = irConcat()
            concat.addArgument(irString("Breimer"))

            irCall(logFunction).also { call ->
                call.putValueArgument(0, arguments)
            }
        }
    }

    fun getPackageNameFromTypeAlias(typeAlias: IrTypeAlias): String? {
        val originalType = typeAlias.expandedType

        return originalType.classFqName?.asString()
    }

}
