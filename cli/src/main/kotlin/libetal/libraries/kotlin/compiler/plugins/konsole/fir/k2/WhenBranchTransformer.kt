package libetal.libraries.kotlin.compiler.plugins.konsole.fir.k2


import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.expressions.IrBranch

class WhenBranchTransformer(builder: DeclarationIrBuilder, private val expressionTransformer: ExpressionTransformer) :
    IrElementTransformerWithBuilder<IrBranch>(builder) {

    override fun visitBranch(branch: IrBranch, data: IrBranch): IrBranch {
        branch.result = branch.result.transform(expressionTransformer, branch.result)
        return super.visitBranch(branch, data)
    }
}
