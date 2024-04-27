package libetal.libraries.kotlin.compiler.plugins.konsole.fir


import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.serialization.proto.IrDefinitelyNotNullType
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrTypeAlias
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class ModuleLowering(private val moduleFragment: IrModuleFragment, private val pluginContext: IrPluginContext) :
    IrElementTransformerVoid() {

    override fun visitFunction(declaration: IrFunction): IrStatement {
       val newStatement =  when (declaration.body) {
            null -> super.visitFunction(declaration)
            else -> declaration.transform(FunctionTransformer(pluginContext, declaration.symbol), declaration) as IrFunction
        }
        return newStatement
    }


}
