package lexicon;

import syntax.Unit;

public class Token implements Unit {
    private final TokenType type;
    private final String value;
    private final int lineNum;

    public Token(TokenType type, String value, int lineNum) {
        this.type = type;
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

    public TokenType getType() {
        return type;
    }

    public int getLineNum() {
        return lineNum;
    }
}
