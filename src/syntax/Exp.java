package syntax;

// Exp → AddExp
public class Exp implements Unit{
    private static final UnitType type = UnitType.EXP;
    private final AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    public String toString() {
        return addExp.toString();
    }

    @Override
    public UnitType getType() {
        return type;
    }
}
