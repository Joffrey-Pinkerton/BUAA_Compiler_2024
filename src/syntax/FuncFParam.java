package syntax;

// FuncFParam → BType Ident ['[' ']'] // k
public class FuncFParam implements Unit {
    private static final UnitType type = UnitType.FUNC_F_PARAM;
    public BType bType;
    public String ident;
    public boolean isArray;

    public FuncFParam(BType bType, String ident, boolean isArray) {
        this.bType = bType;
        this.ident = ident;
        this.isArray = isArray;
    }

    public String toString() {
        return bType + " " + ident + (isArray ? "[]" : "");
    }
}
