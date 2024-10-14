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
        if (decl == null && stmt == null) {
            throw new RuntimeException("Both decl and stmt are null");
        }
        if (decl != null) {
            return decl.toString();
        } else {
            return stmt.toString();
        }
    }

    @Override
    public UnitType getType() {
        return type;
    }

    public boolean isReturnStmt() {
        return stmt != null && stmt.getStmtType().equals(StmtType.RETURN);
    }
}
