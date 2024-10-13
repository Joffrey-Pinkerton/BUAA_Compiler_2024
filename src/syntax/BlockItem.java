package syntax;

// BlockItem → Decl | Stmt
public class BlockItem implements Unit {
    private static final UnitType type = UnitType.BLOCK_ITEM;
    private final Decl decl;
    private final Stmt stmt;

    public BlockItem(Decl decl) {
        this.decl = decl;
        this.stmt = null;
    }

    public BlockItem(Stmt stmt) {
        this.decl = null;
        this.stmt = stmt;
    }

    public String toString() {
        if (decl != null) {
            return decl.toString();
        } else {
            return stmt.toString();
        }
    }
}
