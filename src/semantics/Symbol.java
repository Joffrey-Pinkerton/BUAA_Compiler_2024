package semantics;

import syntax.BType;
import syntax.ConstExp;
import syntax.FuncFParams;

public final class Symbol {
    private final SymbolType type;
    private final String name;
    private final int scopeId;
    private final ConstExp arraySize;
    private final FuncFParams funcFParams;

    private Symbol(SymbolType type, String name, int scopeId, BType bType) {
        this.type = type;
        this.name = name;
        this.scopeId = scopeId;
        this.arraySize = null;
        this.funcFParams = null;
    }

    private Symbol(SymbolType type, String name, int scopeId, ConstExp arraySize) {
        this.type = type;
        this.name = name;
        this.scopeId = scopeId;
        this.arraySize = arraySize;
        this.funcFParams = null;
    }

    private Symbol(SymbolType type, String name, int scopeId, FuncFParams funcFParams) {
        this.type = type;
        this.name = name;
        this.scopeId = scopeId;
        this.arraySize = null;
        this.funcFParams = funcFParams;
    }

    public static Symbol createFuncSymbol(SymbolType type, String name, int scopeId, FuncFParams funcFParams) {
        if (!type.equals(SymbolType.VOID_FUNC) && !type.equals(SymbolType.CHAR_FUNC) && !type.equals(SymbolType.INT_FUNC)) {
            throw new RuntimeException("Wrong Usage!");
        }
        return new Symbol(type, name, scopeId, funcFParams);
    }

    public static Symbol createVarSymbol(SymbolType type, String name, int scopeId, BType bType) {
        if (!type.equals(SymbolType.INT) && !type.equals(SymbolType.CHAR) && !type.equals(SymbolType.CONST_INT) && !type.equals(SymbolType.CONST_CHAR)) {
            throw new RuntimeException("Wrong Usage!");
        }
        return new Symbol(type, name, scopeId, bType);
    }

    public static Symbol createArraySymbol(SymbolType type, String name, int scopeId, ConstExp arraySize) {
        if (!type.equals(SymbolType.CHAR_ARRAY) && !type.equals(SymbolType.INT_ARRAY) && !type.equals(SymbolType.CONST_CHAR_ARRAY) && !type.equals(SymbolType.CONST_INT_ARRAY)) {
            throw new RuntimeException("Wrong Usage!");
        }
        return new Symbol(type, name, scopeId, arraySize);
    }


    @Override
    public String toString() {
        return scopeId + " " + name + " " + type;
    }

    public SymbolType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getScopeId() {
        return scopeId;
    }
}

