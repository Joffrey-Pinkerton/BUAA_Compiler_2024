package syntax;

import java.util.ArrayList;

// Block → '{' { BlockItem } '}'
public class Block implements Unit {
    private static final UnitType type = UnitType.BLOCK;
    private final ArrayList<BlockItem> blockItems = new ArrayList<>();

    public Block(ArrayList<BlockItem> blockItems) {
        this.blockItems.addAll(blockItems);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("{\n");
        for (BlockItem blockItem : blockItems) {
            str.append(blockItem.toString()).append("\n");
        }
        str.append("}");
        return str.toString();
    }
}
