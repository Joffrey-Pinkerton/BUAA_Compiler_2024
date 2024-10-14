package syntax;

import java.util.ArrayList;

import semantics.SymbolType;

// FuncRParams → Exp { ',' Exp }
public class FuncRParams implements Unit {
    private static final UnitType type = UnitType.FUNC_R_PARAMS;
    private final ArrayList<Exp> exps = new ArrayList<>();
    private final ArrayList<SymbolType> types = new ArrayList<>();

    public FuncRParams(ArrayList<Exp> exps, ArrayList<SymbolType> types) {
        this.exps.addAll(exps);
        this.types.addAll(types);
    }

    public boolean checkType(FuncFParams funcFParams) {
        ArrayList<FuncFParam> params = funcFParams.getParams();
        for (int i = 0; i < params.size(); i++) {
            SymbolType rParamType = types.get(i);
            FuncFParam fParam = params.get(i);
            if (rParamType.isArray() && !fParam.isArray() || !rParamType.isArray() && fParam.isArray()) {
                return false;
            }
            if (rParamType.isCharArray() && fParam.isArray() && fParam.getbType().isInt() ||
                    rParamType.isIntArray() && fParam.isArray() && fParam.getbType().isChar()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Exp exp : exps) {
            sb.append(exp.toString());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public int getSize() {
        return exps.size();
    }

    @Override
    public UnitType getType() {
        return type;
    }


}
