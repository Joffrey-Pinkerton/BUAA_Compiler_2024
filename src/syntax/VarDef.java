package syntax;

// VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal // k
public class VarDef implements Unit {
    private static final UnitType type = UnitType.VAR_DEF;
    private final String ident;
    private final ConstExp constExp;
    private final InitVal initVal;

    public VarDef(String ident, ConstExp constExp, InitVal initVal) {
        this.ident = ident;
        this.constExp = constExp;
        this.initVal = initVal;
    }

    public String toString() {
        if (initVal == null) {
            return ident + (constExp == null ? "" : "[" + constExp + "]");
        } else {
            return ident + (constExp == null ? "" : "[" + constExp + "]") + " = " + initVal;
        }
    }

    @Override
    public UnitType getType() {
        return type;
    }

}
