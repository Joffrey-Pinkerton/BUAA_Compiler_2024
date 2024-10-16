import java.util.ArrayList;

public class Lexer {
    private final String source;
    private int curPos;
    private int lineNum;
    private Token curToken;

    private final ArrayList<String> errorLog = new ArrayList<>();

    public Lexer(String source) {
        this.source = source;
        this.curPos = 0;
        this.lineNum = 0;
        this.curToken = null;
    }

    public Token getCurrentToken() {
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
                lineNum++;
                // end of single-line comment
            }
        }
        // multi-line comment
        else if (curPos < source.length() && source.charAt(curPos) == '*') {
            curPos++;
            while (curPos < source.length()) {
                while (curPos < source.length() && source.charAt(curPos) != '*') {
                    if (source.charAt(curPos) == '\n') {
                        lineNum++;
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
        curToken = new Token(TokenType.STRCON, sb.toString());
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
        curToken = new Token(TokenType.CHRCON, sb.toString());
    }

    private void getWord() {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(source.charAt(curPos++));
        } while (curPos < source.length() &&
                (Character.isLetterOrDigit(source.charAt(curPos)) || source.charAt(curPos) == '_'));

        if (TokenType.isReservedWord(sb.toString())) {
            curToken = new Token(TokenType.getReservedType(sb.toString()), sb.toString());
        } else {
            curToken = new Token(TokenType.IDENFR, sb.toString());
        }
    }

    private void getIntConst() {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(source.charAt(curPos++));
        } while (curPos < source.length() && Character.isDigit(source.charAt(curPos)));
        curToken = new Token(TokenType.INTCON, sb.toString());
    }

    private void getOperator() {
        if (curPos + 1 < source.length()) {
            String op = source.substring(curPos, curPos + 2);
            if (TokenType.isOperator(op)) {
                curPos += 2;
                curToken = new Token(TokenType.getOperator(op), op);
                return;
            }
        }
        String op = String.valueOf(source.charAt(curPos));
        if (TokenType.isOperator(op)) {
            curPos++;
            curToken = new Token(TokenType.getOperator(op), op);
        } else {
            curPos++;
            errorLog.add((lineNum + 1) + " " + "a");
        }
    }


    public void next() {
        curToken = null;
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
                        lineNum++;
                    }
                    curPos++;
                }
                // Comments
                else if (ch == '/' && curPos + 1 < source.length()) {
                    char nextCh = source.charAt(curPos + 1);
                    if (nextCh == '/' || nextCh == '*') {
                        handleComment();
                    } else {
                        curToken = new Token(TokenType.DIV, "/");
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

    public ArrayList<String> getErrorLog() {
        return errorLog;
    }
}



