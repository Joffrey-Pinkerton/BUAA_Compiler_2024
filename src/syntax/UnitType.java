package syntax;

import java.util.HashMap;

public enum UnitType {
    COMP_UNIT,
    DECL,
    B_TYPE,
    CONST_DECL,
    VAR_DECL,
    CONST_DEF,
    VAR_DEF,
    CONST_INIT_VAL,
    INIT_VAL,
    FUNC_DEF,
    MAIN_FUNC_DEF,
    FUNC_TYPE,
    FUNC_F_PARAMS,
    FUNC_F_PARAM,
    BLOCK,
    BLOCK_ITEM,
    STMT,
    FOR_STMT,
    EXP,
    COND,
    L_VAL,
    PRIMARY_EXP,
    NUMBER,
    CHARACTER,
    UNARY_EXP,
    UNARY_OP,
    FUNC_R_PARAMS,
    MUL_EXP,
    ADD_EXP,
    REL_EXP,
    EQ_EXP,
    L_AND_EXP,
    L_OR_EXP,
    CONST_EXP,
    TOKEN;

    private static final HashMap<UnitType, String> getTypeName = new HashMap<>() {
        {
            put(COMP_UNIT, "CompUnit");
            put(DECL, "Decl");
            put(CONST_DECL, "ConstDecl");
            put(VAR_DECL, "VarDecl");
            put(CONST_DEF, "ConstDef");
            put(VAR_DEF, "VarDef");
            put(CONST_INIT_VAL, "ConstInitVal");
            put(INIT_VAL, "InitVal");
            put(FUNC_DEF, "FuncDef");
            put(MAIN_FUNC_DEF, "MainFuncDef");
            put(FUNC_TYPE, "FuncType");
            put(FUNC_F_PARAMS, "FuncFParams");
            put(FUNC_F_PARAM, "FuncFParam");
            put(BLOCK, "Block");
            put(BLOCK_ITEM, "BlockItem");
            put(STMT, "Stmt");
            put(FOR_STMT, "ForStmt");
            put(EXP, "Exp");
            put(COND, "Cond");
            put(L_VAL, "LVal");
            put(PRIMARY_EXP, "PrimaryExp");
            put(NUMBER, "Number");
            put(CHARACTER, "Character");
            put(UNARY_EXP, "UnaryExp");
            put(UNARY_OP, "UnaryOp");
            put(FUNC_R_PARAMS, "FuncRParams");
            put(MUL_EXP, "MulExp");
            put(ADD_EXP, "AddExp");
            put(REL_EXP, "RelExp");
            put(EQ_EXP, "EqExp");
            put(L_AND_EXP, "LAndExp");
            put(L_OR_EXP, "LOrExp");
            put(CONST_EXP, "ConstExp");
            put(TOKEN, "Token");
        }
    };

    @Override
    public String toString() {
        return "<" + getTypeName.get(this) + ">";
    }
}

