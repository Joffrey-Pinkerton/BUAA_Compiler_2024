package syntax;

// ForStmt → LVal '=' Exp
public class ForStmt implements Unit{
    private final LVal lval;
    private final Exp exp;

    public ForStmt(LVal lval, Exp exp) {
        this.lval = lval;
        this.exp = exp;
    }

    public String toString() {
        return lval + " = " + exp;
    }
}
