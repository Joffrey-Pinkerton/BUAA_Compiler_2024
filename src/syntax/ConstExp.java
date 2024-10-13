package syntax;

// ConstExp → AddExp [note: Ident used must be constant]
public class ConstExp implements Unit{
    private static final UnitType type = UnitType.CONST_EXP;
    private final AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public String toString() {
        return addExp.toString();
    }
}
