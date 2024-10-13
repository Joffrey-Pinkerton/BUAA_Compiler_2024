package syntax;

import exception.unclassified.UnexpectedErrorException;

// Decl → ConstDecl | VarDecl
public class Decl implements Unit {
    private static final UnitType type = UnitType.DECL;
    private final ConstDecl constDecl;
    private final VarDecl varDecl;

    public Decl(ConstDecl constDecl) {
        this.constDecl = constDecl;
        this.varDecl = null;
    }

    public Decl(VarDecl varDecl) {
        this.constDecl = null;
        this.varDecl = varDecl;
    }

    public String toString() {
        if (constDecl == null && varDecl == null) {
            throw new UnexpectedErrorException("ConstDecl or VarDecl expected");
        }
        return constDecl != null ? constDecl.toString() : varDecl.toString();
    }

    @Override
    public UnitType getType() {
        return type;
    }
}
