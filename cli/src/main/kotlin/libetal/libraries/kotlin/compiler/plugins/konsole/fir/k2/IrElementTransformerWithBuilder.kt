package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer

abstract class IrElementTransformerWithBuilder<D>(val pluginContext: IrPluginContext, val symbol: IrFunctionSymbol) :
    IrElementTransformer<D> {
    val builder by lazy {
        DeclarationIrBuilder(pluginContext, symbol)
    }
}
