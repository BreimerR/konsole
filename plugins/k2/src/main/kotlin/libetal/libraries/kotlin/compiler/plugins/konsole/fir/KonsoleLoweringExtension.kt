package libetal.libraries.kotlin.compiler.plugins.konsole.fir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

open class KonsoleLoweringExtension : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val pass = ModuleLowering(moduleFragment, pluginContext)

        moduleFragment.transform(pass, null)
    }
}
