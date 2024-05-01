package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import libetal.libraries.kotlin.compiler.plugins.konsole.KonsoleConfigs
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

open class KonsoleLoweringExtension(private val konsoleConfigs: KonsoleConfigs) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val pass = ModuleLowering(konsoleConfigs, moduleFragment, pluginContext)
        moduleFragment.transform(pass, null)
    }
}
