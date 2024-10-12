package syntax;

// Character → CharConst
public class Char implements Unit{
    public final char charConst;

    public Char(char charConst) {
        this.charConst = charConst;
    }

    public String toString() {
        return String.valueOf(charConst);
    }
}
