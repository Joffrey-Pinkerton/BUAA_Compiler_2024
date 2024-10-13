package syntax;

import exception.unclassified.UnexpectedErrorException;

import java.util.ArrayList;

// ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';' // i
public class ConstDecl implements Unit{
    private static final UnitType type = UnitType.CONST_DECL;
    private final BType btype;
    private final ArrayList<ConstDef> constDefs = new ArrayList<>();

    public ConstDecl(BType btype, ArrayList<ConstDef> constDefs) {
        this.btype = btype;
        this.constDefs.addAll(constDefs);
    }

    public String toString() {
        StringBuilder constDefsString = new StringBuilder();
        if (constDefs.isEmpty()) {
            throw new UnexpectedErrorException("ConstDecl should have at least one ConstDef");
        } else if (constDefs.size() > 1) {
            constDefsString.append(constDefs.get(0));
        } else {
            for (ConstDef constDef : constDefs) {
                constDefsString.append(", ").append(constDef);
            }
        }
        return "const " + btype + " " + constDefsString + ";";
    }

    @Override
    public UnitType getType() {
        return type;
    }
}
