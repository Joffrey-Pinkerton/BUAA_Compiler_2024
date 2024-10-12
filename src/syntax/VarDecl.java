package syntax;

import java.util.ArrayList;

// VarDecl → BType VarDef { ',' VarDef } ';' // i
public class VarDecl implements Unit{
    public final BType bType;
    public final ArrayList<VarDef> varDefs = new ArrayList<>();

    public VarDecl(BType bType, ArrayList<VarDef> varDefs) {
        this.bType = bType;
        this.varDefs.addAll(varDefs);
    }

    public String toString() {
        StringBuilder varDefsString = new StringBuilder(bType.toString() + " ");
        for (int i = 0; i < varDefs.size(); i++) {
            varDefsString.append(varDefs.get(i).toString());
            if (i != varDefs.size() - 1) {
                varDefsString.append(", ");
            }
        }
        varDefsString.append(";");

        return varDefsString.toString();
    }
}
