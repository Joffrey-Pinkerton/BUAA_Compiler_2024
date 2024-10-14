package syntax;

// BType → 'int' | 'char'
public class BType implements Unit {
    private static final UnitType type = UnitType.B_TYPE;
    private final String btype;

    public BType(String btype) {
        this.btype = btype;
    }

    public String toString() {
        return btype;
    }

    @Override
    public UnitType getType() {
        return type;
    }

    public boolean isChar() {
        return btype.equals("char");
    }

    public boolean isInt() {
        return btype.equals("int");
    }
}
