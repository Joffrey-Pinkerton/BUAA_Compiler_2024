package syntax;

import java.util.ArrayList;

// InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
public class InitVal implements Unit{
    private final Exp exp;
    private final ArrayList<Exp> exps = new ArrayList<>();
    private final String stringConst;

    public InitVal(Exp exp) {
        this.exp = exp;
        this.stringConst = null;
    }

    public InitVal(ArrayList<Exp> exps) {
        this.exp = null;
        this.exps.addAll(exps);
        this.stringConst = null;
    }

    public InitVal(String stringConst) {
        this.exp = null;
        this.stringConst = stringConst;
    }

    public String toString() {
        if (stringConst != null) {
            return stringConst;
        }
        if (exp != null) {
            return exp.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < exps.size(); i++) {
            sb.append(exps.get(i));
            if (i < exps.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
