package syntax;

// BlockItem → Decl | Stmt
public class BlockItem implements Unit {
    public Decl decl;
    public Stmt stmt;

    public BlockItem(Decl decl) {
        this.decl = decl;
    }

    public BlockItem(Stmt stmt) {
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
