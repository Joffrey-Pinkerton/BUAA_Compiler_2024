package semantics;

import syntax.ConstExp;
import syntax.ConstInitVal;
import syntax.FuncFParams;
import syntax.InitVal;

public final class Symbol {
    private final SymbolType type;
    private final String name;
    private final int scopeId;
    private final ConstExp arraySize;
    private final FuncFParams funcFParams;
    private final ConstInitVal constInitVal;
    private final InitVal initVal;

    private Symbol(SymbolType type, String name, int scopeId, ConstInitVal constInitVal) {
        this.type = type;
        this.name = name;
        this.scopeId = scopeId;
        this.arraySize = null;
        this.funcFParams = null;
        this.constInitVal = constInitVal;
        this.initVal = null;
    }

    private Symbol(SymbolType type, String name, int scopeId, InitVal initVal) {
        this.type = type;
        this.name = name;
        this.scopeId = scopeId;
        this.arraySize = null;
        this.funcFParams = null;
        this.constInitVal = null;
        this.initVal = initVal;
    }

    private Symbol(SymbolType type, String name, int scopeId, ConstExp arraySize, ConstInitVal constInitVal) {
        this.type = type;
        this.name = name;
        this.scopeId = scopeId;
        this.arraySize = arraySize;
        this.funcFParams = null;
        this.constInitVal = constInitVal;
        this.initVal = null;
    }

    private Symbol(SymbolType type, String name, int scopeId, ConstExp arraySize, InitVal initVal) {
        this.type = type;
        this.name = name;
        this.scopeId = scopeId;
        this.arraySize = arraySize;
        this.funcFParams = null;
        this.constInitVal = null;
        this.initVal = initVal;
    }


    private Symbol(SymbolType type, String name, int scopeId, FuncFParams funcFParams) {
        this.type = type;
        this.name = name;
        this.scopeId = scopeId;
        this.arraySize = null;
        this.funcFParams = funcFParams;
        this.constInitVal = null;
        this.initVal = null;
    }

    public static Symbol createFuncSymbol(SymbolType type, String name, int scopeId, FuncFParams funcFParams) {
        if (!type.equals(SymbolType.VOID_FUNC) && !type.equals(SymbolType.CHAR_FUNC) && !type.equals(SymbolType.INT_FUNC)) {
            throw new RuntimeException("Wrong Usage!");
        }
        return new Symbol(type, name, scopeId, funcFParams);
    }

    public static Symbol createConstVarSymbol(SymbolType type, String name, int scopeId, ConstInitVal constInitVal) {
        if (!type.equals(SymbolType.CONST_INT) && !type.equals(SymbolType.CONST_CHAR)) {
            throw new RuntimeException("Wrong Usage!");
        }
        return new Symbol(type, name, scopeId, constInitVal);
    }

    public static Symbol createVarSymbol(SymbolType type, String name, int scopeId, InitVal initVal) {
        if (!type.equals(SymbolType.INT) && !type.equals(SymbolType.CHAR)) {
            throw new RuntimeException("Wrong Usage!");
        }
        return new Symbol(type, name, scopeId, initVal);
    }

    public static Symbol createConstArraySymbol(SymbolType type, String name, int scopeId, ConstExp arraySize, ConstInitVal constInitVal) {
        if (!type.equals(SymbolType.CONST_CHAR_ARRAY) && !type.equals(SymbolType.CONST_INT_ARRAY)) {
            throw new RuntimeException("Wrong Usage!");
        }
        return new Symbol(type, name, scopeId, arraySize, constInitVal);
    }

    public static Symbol createArraySymbol(SymbolType type, String name, int scopeId, ConstExp arraySize, InitVal initVal) {
        if (!type.equals(SymbolType.CHAR_ARRAY) && !type.equals(SymbolType.INT_ARRAY)) {
            throw new RuntimeException("Wrong Usage!");
        }
        return new Symbol(type, name, scopeId, arraySize, initVal);
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

