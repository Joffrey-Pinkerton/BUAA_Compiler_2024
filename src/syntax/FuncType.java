package syntax;

//  FuncType → 'void' | 'int' | 'char'
public class FuncType implements Unit{
    private final String funcType;

    public FuncType(String funcType) {
        this.funcType = funcType;
    }

    public String toString() {
        return funcType;
    }
}
