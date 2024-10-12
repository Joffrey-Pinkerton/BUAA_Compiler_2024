package lexicon;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {
    // Identifiers
    IDENFR,     // <Ident>

    // Constant values
    INTCON,     // <IntConst>
    STRCON,     // <StringConst>
    CHRCON,     // <CharConst>

    // Reserved Words
    MAINTK,     // main
    CONSTTK,    // const
    INTTK,      // int
    CHARTK,     // char
    BREAKTK,    // break
    CONTINUETK, // continue
    IFTK,       // if
    ELSETK,     // else
    VOIDTK,     // void
    FORTK,      // for
    GETINTTK,   // getint
    GETCHARTK,  // getchar
    PRINTFTK,   // printf
    RETURNTK,   // return

    // Operators
    NOT,        // !
    AND,        // &&
    OR,         // ||
    MULT,       // *
    DIV,        // /
    MOD,        // %
    LSS,        // <
    LEQ,        // <=
    GRE,        // >
    GEQ,        // >=
    EQL,        // ==
    NEQ,        // !=
    PLUS,       // +
    MINU,       // -
    ASSIGN,     // =
    SEMICN,     // ;
    COMMA,      // ,
    LPARENT,    // (
    RPARENT,    // )
    LBRACK,     // [
    RBRACK,     // ]
    LBRACE,     // {
    RBRACE;      // }

    private static final Map<String, TokenType> reservedWords = new HashMap<>();
    private static final Map<String, TokenType> operators = new HashMap<>();

    static {
        reservedWords.put("main", TokenType.MAINTK);
        reservedWords.put("const", TokenType.CONSTTK);
        reservedWords.put("int", TokenType.INTTK);
        reservedWords.put("char", TokenType.CHARTK);
        reservedWords.put("break", TokenType.BREAKTK);
        reservedWords.put("continue", TokenType.CONTINUETK);
        reservedWords.put("if", TokenType.IFTK);
        reservedWords.put("else", TokenType.ELSETK);
        reservedWords.put("void", TokenType.VOIDTK);
        reservedWords.put("for", TokenType.FORTK);
        reservedWords.put("getint", TokenType.GETINTTK);
        reservedWords.put("getchar", TokenType.GETCHARTK);
        reservedWords.put("printf", TokenType.PRINTFTK);
        reservedWords.put("return", TokenType.RETURNTK);

        operators.put("!", TokenType.NOT);
        operators.put("&&", TokenType.AND);
        operators.put("||", TokenType.OR);
        operators.put("*", TokenType.MULT);
        operators.put("/", TokenType.DIV);
        operators.put("%", TokenType.MOD);
        operators.put("<", TokenType.LSS);
        operators.put("<=", TokenType.LEQ);
        operators.put(">", TokenType.GRE);
        operators.put(">=", TokenType.GEQ);
        operators.put("==", TokenType.EQL);
        operators.put("!=", TokenType.NEQ);
        operators.put("+", TokenType.PLUS);
        operators.put("-", TokenType.MINU);
        operators.put("=", TokenType.ASSIGN);
        operators.put(";", TokenType.SEMICN);
        operators.put(",", TokenType.COMMA);
        operators.put("(", TokenType.LPARENT);
        operators.put(")", TokenType.RPARENT);
        operators.put("[", TokenType.LBRACK);
        operators.put("]", TokenType.RBRACK);
        operators.put("{", TokenType.LBRACE);
        operators.put("}", TokenType.RBRACE);
    }

    public static boolean isReservedWord(String word) {
        return reservedWords.containsKey(word);
    }

    public static boolean isOperator(String word) {
        return operators.containsKey(word);
    }

    public static TokenType getReservedType(String word) {
        return reservedWords.get(word);
    }

    public static TokenType getOperator(String word) {
        return operators.get(word);
    }
}
