package syntax;

import exception.UnexpectedErrorException;

import java.util.ArrayList;

// LAndExp → EqExp | LAndExp '&&' EqExp
public class LAndExp implements Unit {
    private static final UnitType type = UnitType.L_AND_EXP;
    private final ArrayList<EqExp> eqExps = new ArrayList<>();

    public LAndExp(ArrayList<EqExp> eqExps) {
        this.eqExps.addAll(eqExps);
    }

    public String toString() {
        if (eqExps.isEmpty()) {
            throw new UnexpectedErrorException("Empty LAndExp");
        }
        StringBuilder sb = new StringBuilder();
        for (EqExp eqExp : eqExps) {
            sb.append(eqExp.toString());
            sb.append(" && ");
        }
        sb.delete(sb.length() - 4, sb.length());
        return sb.toString();
    }
}
