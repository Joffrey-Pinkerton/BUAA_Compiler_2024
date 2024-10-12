package syntax;

// UnaryOp → '+' | '−' | '!'
public class UnaryOp implements Unit{
    private final String unaryOp;

    public UnaryOp(String unaryOp) {
        this.unaryOp = unaryOp;
    }

    public String toString() {
        return unaryOp;
    }
}
