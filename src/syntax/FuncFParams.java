package syntax;

import java.util.ArrayList;

// FuncFParams → FuncFParam { ',' FuncFParam }
public class FuncFParams implements Unit {
    private static final UnitType type = UnitType.FUNC_F_PARAMS;
    private final ArrayList<FuncFParam> funcFParams = new ArrayList<>();

    public FuncFParams(ArrayList<FuncFParam> funcFParams) {
        this.funcFParams.addAll(funcFParams);
    }

    public String toString() {
        StringBuilder funcFParamsStr = new StringBuilder();
        for (int i = 0; i < funcFParams.size(); i++) {
            funcFParamsStr.append(funcFParams.get(i));
            if (i != funcFParams.size() - 1) {
                funcFParamsStr.append(", ");
            }
        }
        return funcFParamsStr.toString();
    }

    public int getSize() {
        return funcFParams.size();
    }

    @Override
    public UnitType getType() {
        return type;
    }

    public ArrayList<FuncFParam> getParams() {
        return funcFParams;
    }
}
