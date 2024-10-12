package syntax;

//  LVal → Ident ['[' Exp ']'] // k
public class LVal implements Unit{
    public String ident;
    public Exp exp;

    public LVal(String ident) {
        this.ident = ident;
        this.exp = null;
    }

    public LVal(String ident, Exp exp) {
        this.ident = ident;
        this.exp = exp;
    }

    public String toString() {
        return ident + (exp == null ? "" : "[" + exp + "]");
    }
}
