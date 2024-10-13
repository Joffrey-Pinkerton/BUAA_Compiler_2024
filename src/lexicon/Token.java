package lexicon;

import syntax.Unit;
import syntax.UnitType;

public class Token implements Unit {
    private final TokenType tokenType;
    private final String value;
    private final int lineNum;

    public Token(TokenType tokenType, String value, int lineNum) {
        this.tokenType = tokenType;
        this.value = value;
        this.lineNum = lineNum;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getLineNum() {
        return lineNum;
    }

    @Override
    public UnitType getType() {
        return UnitType.TOKEN;
    }
}
