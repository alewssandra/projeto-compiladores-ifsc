package ifsc.compiladores.projeto.LLVM.definitions.jumps;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.definitions.Label;

public class UnconditionalJump implements Fragment {

    private final Label jumpLabel;

    public UnconditionalJump(Label jumpLabel) {
        this.jumpLabel = jumpLabel;
    }
    
    @Override
    public String getText() {
        return String.format("br label %s",
                this.jumpLabel.getNameInIRForm());
    }
    
}
