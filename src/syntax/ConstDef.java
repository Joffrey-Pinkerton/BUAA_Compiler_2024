package syntax;

// ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal // k
public class ConstDef implements Unit {
    private static final UnitType type = UnitType.CONST_DEF;
    private final String ident;
    private final ConstExp constExp;
    private final ConstInitVal constInitVal;

    public ConstDef(String ident, ConstExp constExp, ConstInitVal constInitVal) {
        this.ident = ident;
        this.constExp = constExp;
        this.constInitVal = constInitVal;
    }

    public ConstDef(String ident, ConstInitVal constInitVal) {
        this.ident = ident;
        this.constExp = null;
        this.constInitVal = constInitVal;
    }

    public String getIdent() {
        return ident;
    }

    public String toString() {
        return ident + (constExp == null ? "" : "[" + constExp + "]") + " = " + constInitVal;
    }

    @Override
    public UnitType getType() {
        return type;
    }
}
