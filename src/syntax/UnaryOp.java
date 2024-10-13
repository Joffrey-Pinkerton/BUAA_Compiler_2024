package syntax;

// UnaryOp → '+' | '−' | '!'
public class UnaryOp implements Unit {
    private static final UnitType type = UnitType.UNARY_OP;
    private final String unaryOp;

    public UnaryOp(String unaryOp) {
        this.unaryOp = unaryOp;
    }

    public String toString() {
        return unaryOp;
    }
}
