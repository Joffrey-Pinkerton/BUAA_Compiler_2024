package core;

import exception.RedefinitionException;
import exception.UndefinedIdentifierException;
import semantics.Symbol;
import semantics.SymbolTable;
import semantics.SymbolType;

public class SymbolManager {
    private int scopeCount;
    private SymbolTable curTable;

    public SymbolManager() {
        this.scopeCount = 1;
        this.curTable = new SymbolTable(scopeCount, null);
    }

    public int getScopeId() {
        return curTable.getScopeId();
    }

    public void enterBlock() {
        this.scopeCount++;
        this.curTable = new SymbolTable(scopeCount, curTable);
    }

    public void exitBlock() {
        this.curTable = curTable.getPrevTable();
    }

    public void registerSymbol(Symbol symbol, int lineNum) {
        try {
            if (curTable.exist(symbol.getName())) {
                throw new RedefinitionException("Redefinition of " + symbol.getName(), lineNum);
            }
        } catch (RedefinitionException ignored) {

        }
        curTable.register(symbol.getName(), symbol);
    }

    public Symbol useSymbol(String name, int lineNum) {
        Symbol symbol = lookupSymbol(curTable, name);
        try {
            if (symbol == null) {
                throw new UndefinedIdentifierException("Undefined identifier " + name, lineNum);
            }
        } catch (RedefinitionException ignored) {
        }
        return symbol;
    }

    private Symbol lookupSymbol(SymbolTable table, String name) {
        if (table.exist(name)) {
            return table.getSymbol(name);
        }
        SymbolTable prevTable = table.getPrevTable();
        return prevTable != null ? lookupSymbol(prevTable, name) : null;
    }
}
