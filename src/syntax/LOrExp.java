package syntax;

import exceptions.UnexpectedErrorException;

import java.util.ArrayList;

// LOrExp → LAndExp | LOrExp '||' LAndExp
public class LOrExp implements Unit{
    private final ArrayList<LAndExp> lAndExps = new ArrayList<>();

    public LOrExp(ArrayList<LAndExp> eqExps) {
        this.lAndExps.addAll(eqExps);
    }

    public String toString() {
        if (lAndExps.isEmpty()) {
            throw new UnexpectedErrorException("Empty LOrExp");
        }
        StringBuilder sb = new StringBuilder();
        for (LAndExp lAndExp : lAndExps) {
            sb.append(lAndExp.toString());
            sb.append(" || ");
        }
        sb.delete(sb.length() - 4, sb.length());
        return sb.toString();
    }
}
