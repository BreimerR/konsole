package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer

abstract class IrElementTransformerWithBuilder<D>(val builder: DeclarationIrBuilder) : IrElementTransformer<D> {

    constructor(pluginContext: IrPluginContext, symbol: IrFunctionSymbol) : this(
        DeclarationIrBuilder(
            pluginContext,
            symbol
        )
    )
}
