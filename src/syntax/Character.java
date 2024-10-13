package syntax;

// Character → CharConst
public class Character implements Unit {
    private static final UnitType type = UnitType.CHARACTER;
    private final char charConst;

    public Character(char charConst) {
        this.charConst = charConst;
    }

    public String toString() {
        return String.valueOf(charConst);
    }
}
