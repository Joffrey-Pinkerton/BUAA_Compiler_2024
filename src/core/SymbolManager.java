package core;

import exception.RedefinitionException;
import exception.UndefinedIdentifierException;
import semantics.Symbol;
import semantics.SymbolTable;

public class SymbolManager {
    private static int scopeCount = 1;
    private static SymbolTable curTable = new SymbolTable(scopeCount, null);

    public static int getScopeId() {
        return curTable.getScopeId();
    }

    public static void startAddingFuncFParams() {
        scopeCount++;
        curTable.setNextTable(new SymbolTable(scopeCount, curTable));
        Handler.addSymbolTable();
        curTable = curTable.getNextTable();
    }

    public static void pushScope() {
        scopeCount++;
        Handler.addSymbolTable();
        SymbolTable nextTable = curTable.getNextTable();

        if (nextTable != null) {
            curTable.setNextTable(null);
            curTable = nextTable;
        } else {
            curTable = new SymbolTable(scopeCount, curTable);
            Handler.addSymbolTable();
        }
    }

    public static void endAddingFuncFParams() {
        scopeCount--;
        popScope();
    }

    public static void popScope() {
        curTable = curTable.getPrevTable();
    }

    public static void registerSymbol(Symbol symbol, int lineNum) {
        try {
            if (curTable.exist(symbol.getName())) {
                throw new RedefinitionException("Redefinition of " + symbol.getName(), lineNum);
            }
        } catch (RedefinitionException ignored) {
        }
        curTable.register(symbol.getName(), symbol);
        Handler.addSymbol(symbol.getScopeId(), symbol);
    }

    public static Symbol useSymbol(String name, int lineNum) {
        Symbol symbol = lookupSymbol(curTable, name);
        try {
            if (symbol == null) {
                throw new UndefinedIdentifierException("Undefined identifier " + name, lineNum);
            }
        } catch (UndefinedIdentifierException ignored) {
        }
        return symbol;
    }

    private static Symbol lookupSymbol(SymbolTable table, String name) {
        if (table.exist(name)) {
            return table.getSymbol(name);
        }
        SymbolTable prevTable = table.getPrevTable();
        return prevTable != null ? lookupSymbol(prevTable, name) : null;
    }
}
