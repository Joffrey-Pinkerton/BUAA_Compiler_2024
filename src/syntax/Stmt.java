package syntax;

import exception.UnexpectedErrorException;

import java.util.ArrayList;

//Stmt → LVal '=' Exp ';' // i
//        | [Exp] ';' // i
//        | Block
//        | 'if' '(' Cond ')' Stmt [ 'else' Stmt ] // j
//        | 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
//        | 'break' ';' | 'continue' ';' // i
//        | 'return' [Exp] ';' // i
//        | LVal '=' 'getint''('')'';' // i j
//        | LVal '=' 'getchar''('')'';' // i j
//        | 'printf''('StringConst {','Exp}')'';' // i j
public class Stmt implements Unit {
    private final StmtType type;
    private final ArrayList<Unit> keyParts = new ArrayList<>();

    public Stmt(StmtType type) {
        this.type = type;
    }

    public Stmt(StmtType type, ArrayList<Unit> keyParts) {
        this.type = type;
        this.keyParts.addAll(keyParts);
    }

    public String toString() {
        switch (type) {
            case EMPTY:
                return ";";
            case ASSIGN:
                return /*(LVal)*/ (keyParts.get(0)) + " = " + /*(Exp)*/ (keyParts.get(1)) + ";";
            case EXPR:
                return /*(Exp)*/ keyParts.get(0) + ";";
            case BLOCK:
                return /*(Block)*/ keyParts.get(0).toString();
            case IF:
                return "if (" + /*(Cond)*/ keyParts.get(0) + ") "
                        + /*(Stmt)*/ keyParts.get(1)
                        + (keyParts.size() == 3 ? " else " + /*(Stmt)*/ (keyParts.get(2)) : "");
            case FOR:
                return "for (" + (keyParts.get(0) == null ? "" : /*(ForStmt)*/ keyParts.get(0)) + "; "
                        + (keyParts.get(1) == null ? "" : /*(Cond)*/ keyParts.get(1)) + "; "
                        + (keyParts.get(2) == null ? "" : /*(ForStmt)*/ keyParts.get(2)) + ") "
                        + /*(Stmt)*/ keyParts.get(3);
            case BREAK:
                return "break;";
            case CONTINUE:
                return "continue;";
            case RETURN:
                return "return" + (keyParts.isEmpty() ? "" : " " + /*(Exp)*/ keyParts.get(0)) + ";";
            case GETINT:
                return /*(LVal)*/ (keyParts.get(0)) + " = getint();";
            case GETCHAR:
                return /*(LVal)*/ (keyParts.get(0)) + " = getchar();";
            case PRINTF:
                StringBuilder sb = new StringBuilder();
                sb.append("printf(").append(/*(Token:StrConst)*/ keyParts.get(0));
                for (int i = 1; i < keyParts.size(); i++) {
                    sb.append(", ").append((/*(Exp)*/ keyParts.get(i)));
                }
                sb.append(");");
                return sb.toString();
            default:
                throw new UnexpectedErrorException("Unexpected StmtType: " + type);
        }
    }
}
