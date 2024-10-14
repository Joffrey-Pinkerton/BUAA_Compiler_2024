package lexicon;

import syntax.Unit;
import syntax.UnitType;

public record Token(TokenType tokenType, String value, int lineNum) implements Unit {

    @Override
    public String toString() {
        return value;
    }

    @Override
    public UnitType getType() {
        return UnitType.TOKEN;
    }
}
