package syntax;

//  Cond → LOrExp
public class Cond implements Unit {
    private static final UnitType type = UnitType.COND;
    private final LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.lOrExp = lOrExp;
    }

    public String toString() {
        return lOrExp.toString();
    }

    @Override
    public UnitType getType() {
        return type;
    }

}
