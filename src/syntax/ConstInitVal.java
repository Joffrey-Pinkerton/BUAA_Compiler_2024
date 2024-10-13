package syntax;

import java.util.ArrayList;

// ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
public class ConstInitVal implements Unit {
    private static final UnitType type = UnitType.CONST_INIT_VAL;
    private final ConstExp constExp;
    private final ArrayList<ConstExp> constExps = new ArrayList<>();
    private final String stringConst;

    public ConstInitVal(ConstExp constExp) {
        this.constExp = constExp;
        this.stringConst = null;
    }

    public ConstInitVal(ArrayList<ConstExp> constExps) {
        this.constExp = null;
        this.constExps.addAll(constExps);
        this.stringConst = null;
    }

    public ConstInitVal(String stringConst) {
        this.constExp = null;
        this.stringConst = stringConst;
    }

    public String toString() {
        if (stringConst != null) {
            return stringConst;
        }
        if (constExp != null) {
            return constExp.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < constExps.size(); i++) {
            sb.append(constExps.get(i));
            if (i < constExps.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
