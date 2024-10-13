package syntax;

// MainFuncDef → 'int'' main' '(' ')' Block // j
public class MainFuncDef implements Unit {
    private static final UnitType type = UnitType.MAIN_FUNC_DEF;
    private final Block block;

    public MainFuncDef(Block block) {
        this.block = block;
    }

    public String toString() {
        return "int main() " + block.toString();
    }

    @Override
    public UnitType getType() {
        return type;
    }
}
