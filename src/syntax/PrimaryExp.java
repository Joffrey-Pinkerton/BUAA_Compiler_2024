package syntax;

// PrimaryExp → '(' Exp ')' | LVal | Number | Character// j
public class PrimaryExp implements Unit {
    private final UnitType type = UnitType.PRIMARY_EXP;
    private final Exp exp;
    private final LVal lval;
    private final Number number;
    private final Character character;

    public PrimaryExp(Exp exp) {
        this.exp = exp;
        this.lval = null;
        this.number = null;
        this.character = null;
    }

    public PrimaryExp(LVal lval) {
        this.exp = null;
        this.lval = lval;
        this.number = null;
        this.character = null;
    }

    public PrimaryExp(Number number) {
        this.exp = null;
        this.lval = null;
        this.number = number;
        this.character = null;
    }

    public PrimaryExp(Character character) {
        this.exp = null;
        this.lval = null;
        this.number = null;
        this.character = character;
    }

    public String toString() {
        if (exp != null) {
            return "(" + exp + ")";
        } else if (lval != null) {
            return lval.toString();
        } else if (number != null) {
            return number.toString();
        } else if (character != null) {
            return character.toString();
        } else {
            throw new RuntimeException("All fields are null");
        }
    }

    @Override
    public UnitType getType() {
        return type;
    }
}
