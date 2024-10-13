package syntax;

// PrimaryExp → '(' Exp ')' | LVal | Number | Character// j
public class PrimaryExp implements Unit{
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
            return "(" + exp.toString() + ")";
        } else if (lval != null) {
            return lval.toString();
        } else if (number != null) {
            return number.toString();
        } else {
            return character.toString();
        }
    }

}
