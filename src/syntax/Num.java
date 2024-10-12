package syntax;

// Number → IntConst
public class Num implements Unit{
    public final int intConst;

    public Num(String intConstStr) {
        this.intConst = Integer.parseInt(intConstStr);
    }

    public String toString() {
        return Integer.toString(intConst);
    }
}
