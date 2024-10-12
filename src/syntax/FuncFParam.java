package syntax;

// FuncFParam → BType Ident ['[' ']'] // k
public class FuncFParam implements Unit{
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
