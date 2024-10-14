package core;

import exception.RedefinitionException;
import semantics.Symbol;
import semantics.SymbolTable;
import semantics.SymbolType;

public class Visitor {
    private int scopeId;
    private SymbolTable curTable;

    public Visitor() {
        this.scopeId = 1;
        this.curTable = new SymbolTable(scopeId, null);
    }

    public void enterBlock() {
        this.scopeId++;
        this.curTable = new SymbolTable(scopeId, curTable);
    }

    public void exitBlock() {
        this.curTable = curTable.getPrevTable();
    }

    public void registerSymbol(Symbol symbol, int lineNum) {
        if (curTable.exist(symbol.getName())) {
            throw new RedefinitionException("Redefinition of " + symbol.getName(), lineNum);
        }
        curTable.register(symbol.getName(), symbol);
    }

    public Symbol useSymbol(String name) {
        Symbol symbol = lookupSymbol(curTable, name);
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
