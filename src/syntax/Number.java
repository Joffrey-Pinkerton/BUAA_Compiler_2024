package syntax;

// Number → IntConst
public class Number implements Unit {
    private static final UnitType type = UnitType.NUMBER;
    private final String intConst;

    public Number(String intConstStr) {
        this.intConst = intConstStr;
    }

    public String toString() {
        return intConst;
    }

    @Override
    public UnitType getType() {
        return type;
    }
}
