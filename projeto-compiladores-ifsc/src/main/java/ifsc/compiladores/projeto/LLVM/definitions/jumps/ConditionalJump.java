package ifsc.compiladores.projeto.LLVM.definitions.jumps;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.definitions.Label;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;

public class ConditionalJump implements Fragment {

    private final Variable conditionVariable;
    private final Label trueLabel;
    private final Label falseLabel;

    public ConditionalJump(Variable conditionVariable, Label trueLabel, Label falseLabel) {
        this.conditionVariable = conditionVariable;
        this.trueLabel = trueLabel;
        this.falseLabel = falseLabel;
    }
    
    @Override
    public String getText() {
        return String.format("br i1 %s, label %s, label %s",
                this.conditionVariable.getNameInIRForm(),
                this.trueLabel.getNameInIRForm(),
                this.falseLabel.getNameInIRForm());
    }
    
}
