package top;

import exception.UnexpectedErrorException;
import exception.IllegalTokenException;
import lexicon.Token;
import lexicon.TokenType;

public class Lexer {
    private final String source;
    private int curPos;
    private int lineIndex;
    private Token curToken;
    private Token lastToken;

    private int savePos;
    private int saveLine;
    private Token saveToken;
    private Token saveLastToken;

    public Lexer(String source) {
        this.source = source;
        this.curPos = 0;
        this.lineIndex = 0;
        this.curToken = null;
        this.lastToken = null;

        this.savePos = 0;
        this.saveLine = 0;
        this.saveToken = null;
        this.saveLastToken = null;

        this.next();
    }

    public Token peek() {
        return curToken;
    }

    public boolean notEnd() {
        return curPos < source.length();
    }

    private void handleComment() {
        curPos++;

        // single-line comment
        if (curPos < source.length() && source.charAt(curPos) == '/') {
            do {
                curPos++;
            } while (curPos < source.length() && source.charAt(curPos) != '\n');
            if (curPos < source.length() && source.charAt(curPos) == '\n') {
                curPos++;
                lineIndex++;
                // end of single-line comment
            }
        }
        // multi-line comment
        else if (curPos < source.length() && source.charAt(curPos) == '*') {
            curPos++;
            while (curPos < source.length()) {
                while (curPos < source.length() && source.charAt(curPos) != '*') {
                    if (source.charAt(curPos) == '\n') {
                        lineIndex++;
                    }
                    curPos++;
                }
                while (curPos < source.length() && source.charAt(curPos) == '*') {
                    curPos++;
                }
                if (curPos < source.length() && source.charAt(curPos) == '/') {
                    curPos++;
                    break;
                }
            }
            // end of multi-line comment
        }
    }

    private void getStringConst() {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(source.charAt(curPos++));
            if (source.charAt(curPos) == '\\') {
                sb.append(source.charAt(curPos++));
                sb.append(source.charAt(curPos++));
            }
        } while (curPos < source.length() && source.charAt(curPos) != '\"');
        if (curPos < source.length() && source.charAt(curPos) == '\"') {
            sb.append(source.charAt(curPos++));
        }
        curToken = new Token(TokenType.STRCON, sb.toString(), lineIndex + 1);
    }

    private void getCharConst() {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(source.charAt(curPos++));
            if (source.charAt(curPos) == '\\') {
                sb.append(source.charAt(curPos++));
                sb.append(source.charAt(curPos++));
            }
        } while (curPos < source.length() && source.charAt(curPos) != '\'');
        if (curPos < source.length() && source.charAt(curPos) == '\'') {
            sb.append(source.charAt(curPos++));
        }
        curToken = new Token(TokenType.CHRCON, sb.toString(), lineIndex + 1);
    }

    private void getWord() {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(source.charAt(curPos++));
        } while (curPos < source.length() &&
                (Character.isLetterOrDigit(source.charAt(curPos)) || source.charAt(curPos) == '_'));

        if (TokenType.isReservedWord(sb.toString())) {
            curToken = new Token(TokenType.getReservedType(sb.toString()), sb.toString(), lineIndex + 1);
        } else {
            curToken = new Token(TokenType.IDENFR, sb.toString(), lineIndex + 1);
        }
    }

    private void getIntConst() {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(source.charAt(curPos++));
        } while (curPos < source.length() && Character.isDigit(source.charAt(curPos)));
        curToken = new Token(TokenType.INTCON, sb.toString(), lineIndex + 1);
    }

    private void getOperator() {
        if (curPos + 1 < source.length()) {
            String op = source.substring(curPos, curPos + 2);
            if (TokenType.isOperator(op)) {
                curPos += 2;
                curToken = new Token(TokenType.getOperator(op), op, lineIndex + 1);
                return;
            }
        }
        String op = String.valueOf(source.charAt(curPos));
        if (TokenType.isOperator(op)) {
            curPos++;
            curToken = new Token(TokenType.getOperator(op), op, lineIndex + 1);
        } else {
            if (op.equals("&") || op.equals("|")) {
                handleSingleAndOr(op);
            } else {
                throw new UnexpectedErrorException("Unexpected Token");
            }
        }
    }

    private void handleSingleAndOr(String op) {
        try {
            throw new IllegalTokenException("Unrecognized token", lineIndex + 1);
        } catch (IllegalTokenException e) {
            curPos++;
            curToken = new Token(op.equals("&") ? TokenType.AND : TokenType.OR, op, lineIndex + 1);
        }
    }

    public void next() {
        if (curToken != null) {
            lastToken = curToken;
            Handler.addToken(curToken);
        }
        while (curPos < source.length()) {
            char ch = source.charAt(curPos);
            // StringConst
            if (ch == '\"') {
                getStringConst();
                break;
            }
            // CharConst
            else if (ch == '\'') {
                getCharConst();
                break;
            }
            // Identifiers or Reserved Words
            else if ((Character.isLetter(ch) || ch == '_')) {
                getWord();
                break;
            }
            // IntConst
            else if (Character.isDigit(ch)) {
                getIntConst();
                break;
            }
            // Others
            else {
                // Whitespaces
                if (Character.isWhitespace(ch)) {
                    if (ch == '\n') {
                        lineIndex++;
                    }
                    curPos++;
                }
                // Comments
                else if (ch == '/' && curPos + 1 < source.length()) {
                    char nextCh = source.charAt(curPos + 1);
                    if (nextCh == '/' || nextCh == '*') {
                        handleComment();
                    } else {
                        curToken = new Token(TokenType.DIV, "/", lineIndex + 1);
                        curPos++;
                        break;
                    }
                }
                // Operators
                else {
                    getOperator();
                    break;
                }
            }
        }
    }

    public boolean lookCurrent(TokenType type) {
        return curToken.getType().equals(type);
    }

    public boolean lookAhead(TokenType type) {
        int pos = curPos;
        int line = lineIndex;
        Token token = curToken;

        next();

        Token nextToken = curToken;
        curPos = pos;
        lineIndex = line;
        curToken = token;

        Handler.popToken();

        return nextToken.getType().equals(type);
    }

    public boolean lookDoubleAhead(TokenType type) {
        int pos = curPos;
        int line = lineIndex;
        Token token = curToken;

        next();
        next();

        Token next2Token = curToken;
        curPos = pos;
        lineIndex = line;
        curToken = token;

        Handler.popToken();
        Handler.popToken();

        return next2Token.getType().equals(type);
    }

    public void save() {
        savePos = curPos;
        saveLine = lineIndex;
        saveToken = curToken;
    }

    public void restore() {
        curPos = savePos;
        lineIndex = saveLine;
        curToken = saveToken;
        if (curPos == 0) {
            next();
        }
    }

    public int getLineNum() {
        return lineIndex + 1;
    }

    public Token getLastToken() {
        return lastToken;
    }
}



