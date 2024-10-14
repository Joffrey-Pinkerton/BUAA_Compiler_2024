package semantics;

import java.util.HashMap;

public enum SymbolType {
    CONST_CHAR,
    CHAR,
    VOID_FUNC,
    CONST_INT,
    INT,
    CHAR_FUNC,
    CONST_CHAR_ARRAY,
    CHAR_ARRAY,
    INT_FUNC,
    CONST_INT_ARRAY,
    INT_ARRAY;

    private static final HashMap<SymbolType, String> getTypeName = new HashMap<>() {
        {
            put(CONST_CHAR, "ConstChar");
            put(CHAR, "Char");
            put(VOID_FUNC, "VoidFunc");
            put(CONST_INT, "ConstInt");
            put(INT, "Int");
            put(CHAR_FUNC, "CharFunc");
            put(CONST_CHAR_ARRAY, "ConstCharArray");
            put(CHAR_ARRAY, "CharArray");
            put(INT_FUNC, "IntFunc");
            put(CONST_INT_ARRAY, "ConstIntArray");
            put(INT_ARRAY, "IntArray");
        }
    };

    @Override
    public String toString() {
        return getTypeName.get(this);
    }
}

