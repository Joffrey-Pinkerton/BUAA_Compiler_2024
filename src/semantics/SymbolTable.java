package semantics;

import java.util.HashMap;

public class SymbolTable {
    private final int id;
    private final SymbolTable prevTable;
    private final HashMap<String, Symbol> dictionary;

    public int getScopeId() {
        return id;
    }

    public SymbolTable(int id, SymbolTable prevTable) {
        this.id = id;
        this.prevTable = prevTable;
        this.dictionary = new HashMap<>();
    }

    public SymbolTable getPrevTable() {
        return prevTable;
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


//    public Symbol lookup(String name) {
//        if (dictionary.containsKey(name)) {
//            return dictionary.get(name);
//        } else if (prevTable != null) {
//            return prevTable.lookup(name);
//        } else {
//            return null;
//        }
//    }
}
