package syntax;

import java.util.ArrayList;

// CompUnit → {Decl} {FuncDef} MainFuncDef
public class CompUnit implements Unit{
    private final ArrayList<Decl> decls = new ArrayList<>();
    private final ArrayList<FuncDef> funcDefs = new ArrayList<>();
    private final MainFuncDef mainFuncDef;

    public CompUnit(ArrayList<Decl> decls, ArrayList<FuncDef> funcDefs, MainFuncDef mainFuncDef) {
        this.decls.addAll(decls);
        this.funcDefs.addAll(funcDefs);
        this.mainFuncDef = mainFuncDef;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Decl decl : decls) {
            sb.append(decl).append("\n");
        }
        for (FuncDef funcDef : funcDefs) {
            sb.append(funcDef).append("\n");
        }
        sb.append(mainFuncDef).append("\n");
        return sb.toString();
    }
}
