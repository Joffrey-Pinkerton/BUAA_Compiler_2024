package syntax;

// FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // j
public class FuncDef implements Unit{
    private final FuncType funcType;
    private final String ident;
    private final FuncFParams funcFParams;
    private final Block block;

    public FuncDef(FuncType funcType, String ident, FuncFParams funcFParams, Block block) {
        this.funcType = funcType;
        this.ident = ident;
        this.funcFParams = funcFParams;
        this.block = block;
    }

    public String toString() {
        return funcType + " " + ident + " " + "(" + funcFParams + ")" + block;
    }
}
