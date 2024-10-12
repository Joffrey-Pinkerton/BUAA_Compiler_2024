package syntax;

// BType → 'int' | 'char'
public class BType implements Unit{
    private final String btype;

    public BType(String btype) {
        this.btype = btype;
    }

    public String toString() {
        return btype;
    }
}
