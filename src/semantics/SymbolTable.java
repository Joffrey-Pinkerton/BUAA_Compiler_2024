package semantics;

import core.Handler;

import java.util.HashMap;

public class SymbolTable {
    private final int id;
    private final SymbolTable prevTable;
    private SymbolTable nextTable;
    private final HashMap<String, Symbol> dictionary;

    public int getScopeId() {
        return id;
    }

    public SymbolTable(int id, SymbolTable prevTable) {
        this.id = id;
        this.prevTable = prevTable;
        this.nextTable = null;
        this.dictionary = new HashMap<>();
    }

    public SymbolTable getPrevTable() {
        return prevTable;
    }

    public SymbolTable getNextTable() {
        return nextTable;
    }

    public void setNextTable(SymbolTable nextTable) {
        this.nextTable = nextTable;
    }

    public boolean exist(String name) {
        return dictionary.containsKey(name);
    }

    public void register(String name, Symbol symbol) {
        dictionary.put(name, symbol);
    }

    public Symbol getSymbol(String name) {
        return dictionary.get(name);
    }
}
