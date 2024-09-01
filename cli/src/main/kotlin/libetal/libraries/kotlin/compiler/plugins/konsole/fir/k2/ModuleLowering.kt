package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2

import libetal.libraries.kotlin.compiler.plugins.konsole.KonsoleConfigs
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrFile

import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class ModuleLowering(
    private val konsoleConfigs: KonsoleConfigs,
    private val moduleFragment: IrModuleFragment,
    private val pluginContext: IrPluginContext
) : IrElementTransformerVoid() {
    var line = 0

    override fun visitFunction(declaration: IrFunction): IrStatement {
        val builder = DeclarationIrBuilder(pluginContext, declaration.symbol)
        val newDeclaration = when {
            !konsoleConfigs.enabled -> declaration.transform(KonsoleExpressionDisabler(builder) {
                packageName == "libetal.libraries.konsole"
            }, null)

            else -> {
                var currentDeclaration = declaration

                if (!konsoleConfigs.enableDebug) currentDeclaration =
                    declaration.transform(KonsoleExpressionDisabler(builder) {
                        fqName == "libetal.libraries.konsole.debug"
                    }, null) as IrFunction

                if (!konsoleConfigs.enableInfo) currentDeclaration =
                    declaration.transform(KonsoleExpressionDisabler(builder) {
                        fqName == "libetal.libraries.konsole.info"
                    }, null) as IrFunction

                if (!konsoleConfigs.enableWTF) currentDeclaration =
                    declaration.transform(KonsoleExpressionDisabler(builder) {
                        fqName == "libetal.libraries.konsole.wtf"
                    }, null) as IrFunction

                if (!konsoleConfigs.enableWarning) currentDeclaration =
                    declaration.transform(KonsoleExpressionDisabler(builder) {
                        fqName == "libetal.libraries.konsole.warning"
                    }, null) as IrFunction

                currentDeclaration.transform(
                    FunctionTransformer(moduleFragment, builder),
                    currentDeclaration
                ) as IrFunction
            }
        } as IrFunction

        return super.visitFunction(newDeclaration)
    }

}
