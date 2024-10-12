package syntax;

// Number → IntConst
public class Num implements Unit {
    public final String intConst;

    public Num(String intConstStr) {
        this.intConst = intConstStr;
    }

    public String toString() {
        return intConst;
    }
}
