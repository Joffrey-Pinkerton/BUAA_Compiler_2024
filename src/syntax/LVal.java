package syntax;

//  LVal → Ident ['[' Exp ']'] // k
public class LVal implements Unit {
    private static final UnitType type = UnitType.L_VAL;
    private final String ident;
    private final Exp exp;

    public LVal(String ident, Exp exp) {
        this.ident = ident;
        this.exp = exp;
    }

    public String toString() {
        return ident + (exp == null ? "" : "[" + exp + "]");
    }

    @Override
    public UnitType getType() {
        return type;
    }

    public String getIdent() {
        return ident;
    }
}
