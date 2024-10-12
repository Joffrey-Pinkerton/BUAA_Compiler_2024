package syntax;

import java.util.ArrayList;

// FuncRParams → Exp { ',' Exp }
public class FuncRParams implements Unit{
    private final ArrayList<Exp> exps = new ArrayList<>();

    public FuncRParams(ArrayList<Exp> exps) {
        this.exps.addAll(exps);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Exp exp : exps) {
            sb.append(exp.toString());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}
