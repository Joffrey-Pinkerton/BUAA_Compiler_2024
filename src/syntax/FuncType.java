package syntax;

//  FuncType → 'void' | 'int' | 'char'
public class FuncType implements Unit {
    private static final UnitType type = UnitType.FUNC_TYPE;
    private final String funcType;

    public FuncType(String funcType) {
        this.funcType = funcType;
    }

    public String toString() {
        return funcType;
    }

    @Override
    public UnitType getType() {
        return type;
    }

    public boolean isVoid() {
        return funcType.equals("void");
    }

    public boolean isChar() {
        return funcType.equals("char");
    }

    public boolean isInt() {
        return funcType.equals("int");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FuncType) {
            return funcType.equals(((FuncType) obj).funcType);
        }
        return false;
    }
}
