package syntax;

// UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // j
public class UnaryExp implements Unit{
    private final PrimaryExp primaryExp;
    private final String ident;
    private final FuncRParams funcRParams;
    private final UnaryOp unaryOp;
    private final UnaryExp unaryExp;


    public UnaryExp(PrimaryExp primaryExp) {
        this.primaryExp = primaryExp;
        this.ident = null;
        this.funcRParams = null;
        this.unaryOp = null;
        this.unaryExp = null;
    }

    public UnaryExp(String ident, FuncRParams funcRParams) {
        this.primaryExp = null;
        this.ident = ident;
        this.funcRParams = funcRParams;
        this.unaryOp = null;
        this.unaryExp = null;
    }

    public UnaryExp(UnaryOp unaryOp, UnaryExp unaryExp) {
        this.primaryExp = null;
        this.ident = null;
        this.funcRParams = null;
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    public String toString() {
        if (primaryExp != null) {
            return primaryExp.toString();
        } else if (ident != null) {
            return ident + "(" + (funcRParams != null ? funcRParams.toString() : "") + ")";
        } else {
            return unaryOp.toString() + unaryExp.toString();
        }
    }
}
