package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.extensions.FirIncompatiblePluginAPI
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irConcat
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrTypeAlias
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.name.FqName

class FunctionTransformer(pluginContext: IrPluginContext, symbol: IrFunctionSymbol) :
    IrElementTransformerWithBuilder<IrFunction>(pluginContext, symbol) {

    val IrExpression.shouldTransform
        get(): Boolean = true

    private val typeAnyNullable by lazy {
        pluginContext.irBuiltIns.anyNType
    }

    @OptIn(FirIncompatiblePluginAPI::class)
    val logFunction by lazy {
        pluginContext.referenceFunctions(FqName("kotlin.io.println"))
            .single {
                val parameters = it.owner.valueParameters
                parameters.size == 1 && parameters[0].type == typeAnyNullable
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

        else -> expression
    }

    fun getPackageNameFromTypeAlias(typeAlias: IrTypeAlias): String? {
        val originalType = typeAlias.expandedType

        return originalType.classFqName?.asString()
    }

}
