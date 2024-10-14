package core;

import exception.RedefinitionException;
import exception.UndefinedIdentifierException;
import semantics.Symbol;
import semantics.SymbolTable;

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

    public void startAddingFuncFParams() {
        this.scopeCount++;
        this.curTable.setNextTable(new SymbolTable(scopeCount, curTable));
        Handler.addSymbolTable();
        this.curTable = curTable.getNextTable();
    }

    public void pushScope() {
        this.scopeCount++;
        Handler.addSymbolTable();
        SymbolTable nextTable = curTable.getNextTable();

        if (nextTable != null) {
            this.curTable.setNextTable(null);
            this.curTable = nextTable;
        } else {
            this.curTable = new SymbolTable(scopeCount, curTable);
            Handler.addSymbolTable();
        }
    }

    public void endAddingFuncFParams() {
        this.scopeCount--;
        popScope();
    }

    public void popScope() {
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
        Handler.addSymbol(symbol.getScopeId(), symbol);
    }

    public Symbol useSymbol(String name, int lineNum) {
        Symbol symbol = lookupSymbol(curTable, name);
        try {
            if (symbol == null) {
                throw new UndefinedIdentifierException("Undefined identifier " + name, lineNum);
            }
        } catch (UndefinedIdentifierException ignored) {
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
