package syntax;

public interface Unit {
    UnitType type = null;

    String toString();

    default UnitType getType() {
        return type;
    }
}
