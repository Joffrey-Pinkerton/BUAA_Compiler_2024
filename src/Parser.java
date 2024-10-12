import exceptions.SyntaxErrorException;
import lexicon.Token;
import lexicon.TokenType;
import syntax.*;

import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public CompUnit parseCompUnit() {
        // CompUnit → {Decl} {FuncDef} MainFuncDef
        ArrayList<Decl> decls = new ArrayList<>();
        ArrayList<FuncDef> funcDefs = new ArrayList<>();

        while (lexer.lookCurrent(TokenType.CONSTTK)
                || (lexer.lookCurrent(TokenType.INTTK) || lexer.lookCurrent(TokenType.CHARTK))
                && lexer.lookAhead(TokenType.IDENFR) && lexer.lookDoubleAhead(TokenType.ASSIGN)) {
            decls.add(parseDecl());
        }
        while ((lexer.lookCurrent(TokenType.INTTK) || lexer.lookCurrent(TokenType.CHARTK)
                || lexer.lookCurrent(TokenType.VOIDTK)) && lexer.lookAhead(TokenType.IDENFR)) {
            funcDefs.add(parseFuncDef());
        }
        MainFuncDef mainFuncDef = parseMainFuncDef();

        Handler.pushOutput("<CompUnit>");
        return new CompUnit(decls, funcDefs, mainFuncDef);
    }

    public Decl parseDecl() {
        // Decl → ConstDecl | VarDecl
        Decl decl = lexer.lookCurrent(TokenType.CONSTTK) ? new Decl(parseConstDecl()) : new Decl(parseVarDecl());
        Handler.pushOutput("<Decl>");
        return decl;
    }

    public ConstDecl parseConstDecl() {
        // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';' // i
        if (!lexer.lookCurrent(TokenType.CONSTTK)) {
            throw new SyntaxErrorException("Expect 'const', but get " + lexer.peek());
        }
        lexer.next();
        BType bType = parseBType();

        ArrayList<ConstDef> constDefs = new ArrayList<>();
        constDefs.add(parseConstDef());
        while (lexer.lookCurrent(TokenType.COMMA)) {
            lexer.next();
            constDefs.add(parseConstDef());
        }
        if (!lexer.lookCurrent(TokenType.SEMICN)) {
            throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
        }
        lexer.next();
        Handler.pushOutput("<ConstDecl>");
        return new ConstDecl(bType, constDefs);
    }

    public BType parseBType() {
        // BType → 'int' | 'char'
        if (!lexer.lookCurrent(TokenType.INTTK) && !lexer.lookCurrent(TokenType.CHARTK)) {
            throw new SyntaxErrorException("Expect 'int' or 'char', but get " + lexer.peek());
        }
        String bType = lexer.lookCurrent(TokenType.INTTK) ? "int" : "char";
        lexer.next();
        Handler.pushOutput("<BType>");
        return new BType(bType);
    }

    public ConstDef parseConstDef() {
        // ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal // k
        if (!lexer.lookCurrent(TokenType.IDENFR)) {
            throw new SyntaxErrorException("Expect an identifier, but get " + lexer.peek());
        }
        String ident = lexer.peek().getValue();
        lexer.next();

        boolean isArray = false;
        ConstExp constExp = null;
        if (lexer.lookCurrent(TokenType.LBRACK)) {
            isArray = true;
            lexer.next();
            constExp = parseConstExp();
            if (!lexer.lookCurrent(TokenType.RBRACK)) {
                throw new SyntaxErrorException("Expect ']', but get " + lexer.peek());
            }
            lexer.next();
        }
        if (!lexer.lookCurrent(TokenType.ASSIGN)) {
            throw new SyntaxErrorException("Expect '=', but get " + lexer.peek());
        }
        lexer.next();
        ConstInitVal constInitVal = parseConstInitVal();

        Handler.pushOutput("<ConstDef>");
        return isArray ? new ConstDef(ident, constExp, constInitVal) : new ConstDef(ident, constInitVal);
    }

    public ConstInitVal parseConstInitVal() {
        // ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
        if (lexer.lookCurrent(TokenType.STRCON)) {
            String str = lexer.peek().getValue();
            lexer.next();
            Handler.pushOutput("<ConstInitVal>");
            return new ConstInitVal(str);
        } else if (lexer.lookCurrent(TokenType.LBRACE)) {
            lexer.next();
            if (lexer.lookCurrent(TokenType.RBRACE)) {
                lexer.next();
                return new ConstInitVal(new ArrayList<>());
            }
            ArrayList<ConstExp> constExps = new ArrayList<>();
            constExps.add(parseConstExp());
            while (lexer.lookCurrent(TokenType.COMMA)) {
                lexer.next();
                constExps.add(parseConstExp());
            }
            if (!lexer.lookCurrent(TokenType.RBRACE)) {
                throw new SyntaxErrorException("Expect '}', but get " + lexer.peek());
            }
            lexer.next();
            Handler.pushOutput("<ConstInitVal>");
            return new ConstInitVal(constExps);
        } else {
            ConstExp constExp = parseConstExp();
            Handler.pushOutput("<ConstInitVal>");
            return new ConstInitVal(constExp);
        }
    }

    public VarDecl parseVarDecl() {
        // VarDecl → BType VarDef { ',' VarDef } ';' // i
        BType bType = parseBType();
        ArrayList<VarDef> varDefs = new ArrayList<>();
        varDefs.add(parseVarDef());
        while (lexer.lookCurrent(TokenType.COMMA)) {
            lexer.next();
            varDefs.add(parseVarDef());
        }
        if (!lexer.lookCurrent(TokenType.SEMICN)) {
            throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
        }
        lexer.next();
        Handler.pushOutput("<VarDecl>");
        return new VarDecl(bType, varDefs);
    }

    public VarDef parseVarDef() {
        // VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal // k
        if (!lexer.lookCurrent(TokenType.IDENFR)) {
            throw new SyntaxErrorException("Expect an identifier, but get " + lexer.peek());
        }
        String ident = lexer.peek().getValue();
        lexer.next();

        ConstExp constExp = null;
        if (lexer.lookCurrent(TokenType.LBRACK)) {
            lexer.next();
            constExp = parseConstExp();
            if (!lexer.lookCurrent(TokenType.RBRACK)) {
                throw new SyntaxErrorException("Expect ']', but get " + lexer.peek());
            }
            lexer.next();
        }
        if (lexer.lookCurrent(TokenType.ASSIGN)) {
            lexer.next();
            InitVal initVal = parseInitVal();
            Handler.pushOutput("<VarDef>");
            return new VarDef(ident, constExp, initVal);
        } else {
            Handler.pushOutput("<VarDef>");
            return new VarDef(ident, constExp);
        }
    }

    public InitVal parseInitVal() {
        // InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
        if (lexer.lookCurrent(TokenType.STRCON)) {
            String str = lexer.peek().getValue();
            lexer.next();
            Handler.pushOutput("<InitVal>");
            return new InitVal(str);
        } else if (lexer.lookCurrent(TokenType.LBRACE)) {
            lexer.next();
            if (lexer.lookCurrent(TokenType.RBRACE)) {
                lexer.next();
                Handler.pushOutput("<InitVal>");
                return new InitVal(new ArrayList<>());
            }
            ArrayList<Exp> exps = new ArrayList<>();
            exps.add(parseExp());
            while (lexer.lookCurrent(TokenType.COMMA)) {
                lexer.next();
                exps.add(parseExp());
            }
            if (!lexer.lookCurrent(TokenType.RBRACE)) {
                throw new SyntaxErrorException("Expect '}', but get " + lexer.peek());
            }
            lexer.next();
            Handler.pushOutput("<InitVal>");
            return new InitVal(exps);
        } else {
            Exp exp = parseExp();
            Handler.pushOutput("<InitVal>");
            return new InitVal(exp);
        }
    }

    public FuncDef parseFuncDef() {
        // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // j
        FuncType funcType = parseFuncType();
        if (!lexer.lookCurrent(TokenType.IDENFR)) {
            throw new SyntaxErrorException("Expect an identifier, but get " + lexer.peek());
        }
        String funcName = lexer.peek().getValue();

        lexer.next();
        if (!lexer.lookCurrent(TokenType.LPARENT)) {
            throw new SyntaxErrorException("Expect '(', but get " + lexer.peek());
        }
        lexer.next();
        FuncFParams funcFParams = parseFuncFParams();
        if (!lexer.lookCurrent(TokenType.RPARENT)) {
            throw new SyntaxErrorException("Expect ')', but get " + lexer.peek());
        }
        lexer.next();
        Block block = parseBlock();
        Handler.pushOutput("<FuncDef>");
        return new FuncDef(funcType, funcName, funcFParams, block);
    }

    public FuncType parseFuncType() {
        // FuncType → 'void' | 'int' | 'char'
        if (!lexer.lookCurrent(TokenType.INTTK) && !lexer.lookCurrent(TokenType.CHARTK) && !lexer.lookCurrent(TokenType.VOIDTK)) {
            throw new SyntaxErrorException("Expect 'int', 'char', or 'void', but get " + lexer.peek());
        }
        String funcType = lexer.lookCurrent(TokenType.INTTK) ? "int" : lexer.lookCurrent(TokenType.CHARTK) ? "char" : "void";
        lexer.next();
        Handler.pushOutput("<FuncType>");
        return new FuncType(funcType);
    }

    public MainFuncDef parseMainFuncDef() {
        // MainFuncDef → 'int' 'main' '(' ')' Block // j
        if (!lexer.lookCurrent(TokenType.INTTK)) {
            throw new SyntaxErrorException("Expect 'int', but get " + lexer.peek());
        }
        lexer.next();
        if (!lexer.lookCurrent(TokenType.MAINTK)) {
            throw new SyntaxErrorException("Expect 'main', but get " + lexer.peek());
        }

        lexer.next();
        if (!lexer.lookCurrent(TokenType.LPARENT)) {
            throw new SyntaxErrorException("Expect '(', but get " + lexer.peek());
        }
        lexer.next();
        if (!lexer.lookCurrent(TokenType.RPARENT)) {
            throw new SyntaxErrorException("Expect ')', but get " + lexer.peek());
        }
        lexer.next();
        Block block = parseBlock();
        Handler.pushOutput("<MainFuncDef>");
        return new MainFuncDef(block);
    }

    public FuncFParams parseFuncFParams() {
        // FuncFParams → FuncFParam { ',' FuncFParam }
        ArrayList<FuncFParam> funcFParams = new ArrayList<>();

        funcFParams.add(parseFuncFParam());

        while (lexer.lookCurrent(TokenType.COMMA)) {
            lexer.next();
            funcFParams.add(parseFuncFParam());
        }
        Handler.pushOutput("<FuncFParams>");
        return new FuncFParams(funcFParams);
    }

    public FuncFParam parseFuncFParam() {
        // FuncFParam → BType Ident ['[' ']'] // k
        BType bType = parseBType();
        if (!lexer.lookCurrent(TokenType.IDENFR)) {
            throw new SyntaxErrorException("Expect an identifier, but get " + lexer.peek());
        }
        String ident = lexer.peek().getValue();
        lexer.next();

        boolean isArray = false;
        if (lexer.lookCurrent(TokenType.LBRACK)) {
            isArray = true;
            lexer.next();
            if (!lexer.lookCurrent(TokenType.RBRACK)) {
                throw new SyntaxErrorException("Expect ']', but get " + lexer.peek());
            }
            lexer.next();
        }
        Handler.pushOutput("<FuncFParam>");
        return new FuncFParam(bType, ident, isArray);
    }

    public Block parseBlock() {
        // Block → '{' { BlockItem } '}'
        if (!lexer.lookCurrent(TokenType.LBRACE)) {
            throw new SyntaxErrorException("Expect '{', but get " + lexer.peek());
        }
        lexer.next();
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        while (!lexer.lookCurrent(TokenType.RBRACE)) {
            blockItems.add(parseBlockItem());
        }
        lexer.next();
        Handler.pushOutput("<Block>");
        return new Block(blockItems);
    }

    public BlockItem parseBlockItem() {
        // BlockItem → Decl | Stmt
        if (lexer.lookCurrent(TokenType.CONSTTK) || (lexer.lookCurrent(TokenType.INTTK) || lexer.lookCurrent(TokenType.CHARTK))) {
            Decl decl = parseDecl();
            Handler.pushOutput("<BlockItem>");
            return new BlockItem(decl);
        } else {
            Stmt stmt = parseStmt();
            Handler.pushOutput("<BlockItem>");
            return new BlockItem(parseStmt());
        }
    }

    private Stmt parseStmtIf() {
        ArrayList<Unit> units = new ArrayList<>();
        lexer.next();
        if (!lexer.lookCurrent(TokenType.LPARENT)) {
            throw new SyntaxErrorException("Expect '(', but get " + lexer.peek());
        }
        lexer.next();
        Cond cond = parseCond();
        units.add(cond);
        if (!lexer.lookCurrent(TokenType.RPARENT)) {
            throw new SyntaxErrorException("Expect ')', but get " + lexer.peek());
        }
        lexer.next();
        Stmt thenStmt = parseStmt();
        units.add(thenStmt);

        if (lexer.lookCurrent(TokenType.ELSETK)) {
            lexer.next();
            Stmt elseStmt = parseStmt();
            units.add(elseStmt);
        }
        Handler.pushOutput("<Stmt>");
        return new Stmt(StmtType.IF, units);
    }

    private Stmt parseStmtFor() {
        ArrayList<Unit> units = new ArrayList<>();
        lexer.next();
        if (!lexer.lookCurrent(TokenType.LPARENT)) {
            throw new SyntaxErrorException("Expect '(', but get " + lexer.peek());
        }
        lexer.next();
        // ForStmt_1 exists or not?
        if (lexer.lookCurrent(TokenType.SEMICN)) {
            lexer.next();
            units.add(null);
        } else {
            ForStmt forStmt = parseForStmt();
            units.add(forStmt);
            if (!lexer.lookCurrent(TokenType.SEMICN)) {
                throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
            }
            lexer.next();
        }
        // Cond exists or not?
        if (lexer.lookCurrent(TokenType.SEMICN)) {
            lexer.next();
            units.add(null);
        } else {
            Cond cond = parseCond();
            units.add(cond);
            if (!lexer.lookCurrent(TokenType.SEMICN)) {
                throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
            }
            lexer.next();
        }
        // ForStmt_2 exists or not?
        if (lexer.lookCurrent(TokenType.RPARENT)) {
            lexer.next();
            units.add(null);
        } else {
            ForStmt forStmt = parseForStmt();
            units.add(forStmt);
            if (!lexer.lookCurrent(TokenType.RPARENT)) {
                throw new SyntaxErrorException("Expect ')', but get " + lexer.peek());
            }
            lexer.next();
        }
        Stmt stmt = parseStmt();
        units.add(stmt);
        Handler.pushOutput("<Stmt>");
        return new Stmt(StmtType.FOR, units);
    }

    private Stmt parseStmtPrintf() {
        ArrayList<Unit> units = new ArrayList<>();
        lexer.next();
        if (!lexer.lookCurrent(TokenType.LPARENT)) {
            throw new SyntaxErrorException("Expect '(', but get " + lexer.peek());
        }
        lexer.next();
        if (!lexer.lookCurrent(TokenType.STRCON)) {
            throw new SyntaxErrorException("Expect a string constant, but get " + lexer.peek());
        }
        units.add(lexer.peek());
        lexer.next();

        while (lexer.lookCurrent(TokenType.COMMA)) {
            lexer.next();
            units.add(parseExp());
        }
        if (!lexer.lookCurrent(TokenType.RPARENT)) {
            throw new SyntaxErrorException("Expect ')', but get " + lexer.peek());
        }
        lexer.next();
        if (!lexer.lookCurrent(TokenType.SEMICN)) {
            throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
        }
        lexer.next();
        return new Stmt(StmtType.PRINTF, units);
    }

    private Stmt parseStmtOther() {
        // ASSIGN? EXPR? GETINT? GETCHAR?
        ArrayList<Unit> units = new ArrayList<>();
        if (lexer.lookCurrent(TokenType.PLUS) || lexer.lookCurrent(TokenType.MINU) || lexer.lookCurrent(TokenType.NOT) || // UnaryOp->UnaryExp->EXPR
                lexer.lookCurrent(TokenType.INTCON) || lexer.lookCurrent(TokenType.CHRCON) || lexer.lookCurrent(TokenType.LPARENT) || // Number, Character,(->PrimaryExp->EXPR
                lexer.lookCurrent(TokenType.IDENFR) && lexer.lookAhead(TokenType.LPARENT) // UnaryExp->PrimaryExp->LVal->Ident->EXPR
        ) {
            // EXPR, definitely
            units.add(parseExp());
            if (lexer.lookCurrent(TokenType.SEMICN)) {
                lexer.next();
                return new Stmt(StmtType.EXPR, new ArrayList<>(units));
            } else {
                throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
            }
        } else {
            // definitely starts with LVal(could be EXPR as well!!!)
            LVal lVal = parseLVal();
            units.add(lVal);
            if (lexer.lookCurrent(TokenType.SEMICN)) { // EXPR
                lexer.next();
                return new Stmt(StmtType.EXPR, units);
            }
            if (!lexer.lookCurrent(TokenType.ASSIGN)) {
                throw new SyntaxErrorException("Expect '=', but get " + lexer.peek());
            }
            lexer.next();
            if (lexer.lookCurrent(TokenType.GETINTTK) || lexer.lookCurrent(TokenType.GETCHARTK)) { // GETINT+GETCHAR
                StmtType stmtType = lexer.lookCurrent(TokenType.GETINTTK) ? StmtType.GETINT : StmtType.GETCHAR;
                lexer.next();
                if (!lexer.lookCurrent(TokenType.LPARENT)) {
                    throw new SyntaxErrorException("Expect '(', but get " + lexer.peek());
                }
                lexer.next();
                if (!lexer.lookCurrent(TokenType.RPARENT)) {
                    throw new SyntaxErrorException("Expect ')', but get " + lexer.peek());
                }
                lexer.next();
                if (!lexer.lookCurrent(TokenType.SEMICN)) {
                    throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
                }
                lexer.next();
                return new Stmt(stmtType, units);
            } else { // ASSIGN
                units.add(parseExp());
                if (!lexer.lookCurrent(TokenType.SEMICN)) {
                    throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
                }
                lexer.next();
                return new Stmt(StmtType.ASSIGN, units);
            }
        }
    }

    private Stmt parseStmtBlock() {
        Block block = parseBlock();
        Handler.pushOutput("<Stmt>");
        return new Stmt(StmtType.BLOCK, new ArrayList<>() {{
            add(block);
        }});
    }

    private Stmt parseStmtReturn() {
        lexer.next();
        if (lexer.lookCurrent(TokenType.SEMICN)) {
            lexer.next();
            Handler.pushOutput("<Stmt>");
            return new Stmt(StmtType.RETURN);
        } else {
            ArrayList<Unit> units = new ArrayList<>();
            units.add(parseExp());
            if (!lexer.lookCurrent(TokenType.SEMICN)) {
                throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
            }
            lexer.next();
            Handler.pushOutput("<Stmt>");
            return new Stmt(StmtType.RETURN, units);
        }
    }

    private Stmt parseStmtBreak() {
        lexer.next();
        if (!lexer.lookCurrent(TokenType.SEMICN)) {
            throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
        }
        lexer.next();
        Handler.pushOutput("<Stmt>");
        return new Stmt(StmtType.BREAK);
    }

    private Stmt parseStmtContinue() {
        lexer.next();
        if (!lexer.lookCurrent(TokenType.SEMICN)) {
            throw new SyntaxErrorException("Expect ';', but get " + lexer.peek());
        }
        lexer.next();
        Handler.pushOutput("<Stmt>");
        return new Stmt(StmtType.CONTINUE);
    }

    private Stmt parseStmtEmpty() {
        lexer.next();
        Handler.pushOutput("<Stmt>");
        return new Stmt(StmtType.EMPTY);
    }

    public Stmt parseStmt() {
        //ASSIGN          //Stmt → LVal '=' Exp ';' // i
        //EMPTY+EXPR       //        | [Exp] ';' // i
        //BLOCK           //        | Block
        //IF              //        | 'if' '(' Cond ')' Stmt [ 'else' Stmt ] // j
        //FOR             //        | 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
        //BREAK+CONTINUE  //        | 'break' ';' | 'continue' ';' // i
        //RETURN          //        | 'return' [Exp] ';' // i
        //GETINT          //        | LVal '=' 'getint''('')'';' // i j
        //GETCHAR         //        | LVal '=' 'getchar''('')'';' // i j
        //PRINTF          //        | 'printf''('StringConst {','Exp}')'';' // i j
        return switch (lexer.peek().getType()) {
            case LBRACE -> parseStmtBlock(); // BLOCK
            case IFTK -> parseStmtIf(); // IF
            case FORTK -> parseStmtFor(); // FOR
            case BREAKTK -> parseStmtBreak(); // BREAK
            case CONTINUETK -> parseStmtContinue(); // CONTINUE
            case RETURNTK -> parseStmtReturn(); // RETURN
            case PRINTFTK -> parseStmtPrintf(); // PRINTF
            case SEMICN -> parseStmtEmpty(); // EMPTY
            default -> parseStmtOther(); // ASSIGN? EXPR? GETINT? GETCHAR?
        };
    }

    public ForStmt parseForStmt() {
        // ForStmt → LVal '=' Exp
        LVal lVal = parseLVal();
        if (!lexer.lookCurrent(TokenType.ASSIGN)) {
            throw new SyntaxErrorException("Expect '=', but get " + lexer.peek());
        }
        lexer.next();
        Exp exp = parseExp();
        Handler.pushOutput("<ForStmt>");
        return new ForStmt(lVal, exp);
    }

    public Exp parseExp() {
        // Exp → AddExp
        AddExp addExp = parseAddExp();
        Handler.pushOutput("<Exp>");
        return new Exp(addExp);
    }

    public Cond parseCond() {
        // Cond → LOrExp
        LOrExp lOrExp = parseLOrExp();
        Handler.pushOutput("<Cond>");
        return new Cond(lOrExp);
    }

    public LVal parseLVal() {
        // LVal → Ident ['[' Exp ']'] // k
        if (!lexer.lookCurrent(TokenType.IDENFR)) {
            throw new SyntaxErrorException("Expect an identifier, but get " + lexer.peek());
        }
        String ident = lexer.peek().getValue();
        lexer.next();
        if (lexer.lookCurrent(TokenType.LBRACK)) {
            lexer.next();
            Exp exp = parseExp();
            if (!lexer.lookCurrent(TokenType.RBRACK)) {
                throw new SyntaxErrorException("Expect ']', but get " + lexer.peek());
            }
            lexer.next();
            Handler.pushOutput("<LVal>");
            return new LVal(ident, exp);
        } else {
            Handler.pushOutput("<LVal>");
            return new LVal(ident);
        }
    }

    public PrimaryExp parsePrimaryExp() {
        // PrimaryExp → '(' Exp ')' | LVal | Number | Character // j
        if (lexer.lookCurrent(TokenType.LPARENT)) {
            lexer.next();
            Exp exp = parseExp();
            if (!lexer.lookCurrent(TokenType.RPARENT)) {
                throw new SyntaxErrorException("Expect ')', but get " + lexer.peek());
            }
            lexer.next();
            Handler.pushOutput("<PrimaryExp>");
            return new PrimaryExp(exp);
        } else if (lexer.lookCurrent(TokenType.INTCON)) {
            Num number = parseNumber();
            Handler.pushOutput("<PrimaryExp>");
            return new PrimaryExp(number);
        } else if (lexer.lookCurrent(TokenType.CHRCON)) {
            Char character = parseCharacter();
            Handler.pushOutput("<PrimaryExp>");
            return new PrimaryExp(character);
        } else {
            LVal lVal = parseLVal();
            Handler.pushOutput("<PrimaryExp>");
            return new PrimaryExp(lVal);
        }
    }

    public Num parseNumber() {
        if (lexer.lookCurrent(TokenType.INTCON)) {
            String intConst = lexer.peek().getValue();
            lexer.next();
            Handler.pushOutput("<Number>");
            return new Num(intConst);
        } else {
            throw new SyntaxErrorException("Expect an integer constant, but get " + lexer.peek());
        }
    }

    public Char parseCharacter() {
        if (lexer.lookCurrent(TokenType.CHRCON)) {
            char charConst = lexer.peek().getValue().charAt(0);
            lexer.next();
            Handler.pushOutput("<Character>");
            return new Char(charConst);
        } else {
            throw new SyntaxErrorException("Expect a character constant, but get " + lexer.peek());
        }
    }

    public UnaryExp parseUnaryExp() {
        // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // j
        if (lexer.lookCurrent(TokenType.IDENFR) && lexer.lookAhead(TokenType.LPARENT)) {
            String ident = lexer.peek().getValue();
            lexer.next();
            lexer.next();
            if (lexer.lookCurrent(TokenType.RPARENT)) {
                lexer.next();
                Handler.pushOutput("<UnaryExp>");
                return new UnaryExp(ident, null);
            }
            FuncRParams funcRParams = parseFuncRParams();
            if (!lexer.lookCurrent(TokenType.RPARENT)) {
                throw new SyntaxErrorException("Expect ')', but get " + lexer.peek());
            }
            lexer.next();
            Handler.pushOutput("<UnaryExp>");
            return new UnaryExp(ident, funcRParams);
        } else if (lexer.lookCurrent(TokenType.PLUS) || lexer.lookCurrent(TokenType.MINU) || lexer.lookCurrent(TokenType.NOT)) {
            UnaryOp unaryOp = parseUnaryOp();
            UnaryExp unaryExp = parseUnaryExp();
            Handler.pushOutput("<UnaryExp>");
            return new UnaryExp(unaryOp, unaryExp);
        } else {
            Handler.pushOutput("<UnaryExp>");
            return new UnaryExp(parsePrimaryExp());
        }
    }

    public UnaryOp parseUnaryOp() {
        // UnaryOp → '+' | '−' | '!'
        if (lexer.lookCurrent(TokenType.PLUS) || lexer.lookCurrent(TokenType.MINU) || lexer.lookCurrent(TokenType.NOT)) {
            lexer.next();
            Handler.pushOutput("<UnaryOp>");
            return new UnaryOp(lexer.peek().getValue());
        } else {
            throw new SyntaxErrorException("Expect '+', '-', or '!', but get " + lexer.peek());
        }
    }

    public FuncRParams parseFuncRParams() {
        // FuncRParams → Exp { ',' Exp }
        ArrayList<Exp> exps = new ArrayList<>();
        exps.add(parseExp());
        while (lexer.lookCurrent(TokenType.COMMA)) {
            lexer.next();
            exps.add(parseExp());
        }
        Handler.pushOutput("<FuncRParams>");
        return new FuncRParams(exps);
    }

    public MulExp parseMulExp() {
        // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
        ArrayList<UnaryExp> unaryExps = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();

        unaryExps.add(parseUnaryExp());
        while (lexer.lookCurrent(TokenType.MULT) || lexer.lookCurrent(TokenType.DIV) || lexer.lookCurrent(TokenType.MOD)) {
            ops.add(lexer.peek());
            lexer.next();
            unaryExps.add(parseUnaryExp());
        }
        Handler.pushOutput("<MulExp>");
        return new MulExp(unaryExps, ops);
    }

    public AddExp parseAddExp() {
        // AddExp → MulExp | AddExp ('+' | '−') MulExp
        ArrayList<MulExp> mulExps = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();

        mulExps.add(parseMulExp());
        while (lexer.lookCurrent(TokenType.PLUS) || lexer.lookCurrent(TokenType.MINU)) {
            ops.add(lexer.peek());
            lexer.next();
            mulExps.add(parseMulExp());
        }
        Handler.pushOutput("<AddExp>");
        return new AddExp(mulExps, ops);
    }

    public RelExp parseRelExp() {
        // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
        ArrayList<AddExp> addExps = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();

        addExps.add(parseAddExp());
        while (lexer.lookCurrent(TokenType.LSS) || lexer.lookCurrent(TokenType.LEQ) || lexer.lookCurrent(TokenType.GRE)
                || lexer.lookCurrent(TokenType.GEQ)) {
            ops.add(lexer.peek());
            lexer.next();
            addExps.add(parseAddExp());
        }
        Handler.pushOutput("<RelExp>");
        return new RelExp(addExps, ops);

    }

    public EqExp parseEqExp() {
        // EqExp → RelExp | EqExp ('==' | '!=') RelExp
        ArrayList<RelExp> relExps = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();

        relExps.add(parseRelExp());
        while (lexer.lookCurrent(TokenType.EQL) || lexer.lookCurrent(TokenType.NEQ)) {
            ops.add(lexer.peek());
            lexer.next();
            relExps.add(parseRelExp());
        }
        Handler.pushOutput("<EqExp>");
        return new EqExp(relExps, ops);
    }

    public LAndExp parseLAndExp() {
        // LAndExp → EqExp | LAndExp '&&' EqExp
        ArrayList<EqExp> eqExps = new ArrayList<>();
        eqExps.add(parseEqExp());
        while (lexer.lookCurrent(TokenType.AND)) {
            lexer.next();
            eqExps.add(parseEqExp());
        }
        Handler.pushOutput("<LAndExp>");
        return new LAndExp(eqExps);
    }

    public LOrExp parseLOrExp() {
        // LOrExp → LAndExp | LOrExp '||' LAndExp
        ArrayList<LAndExp> lAndExps = new ArrayList<>();
        lAndExps.add(parseLAndExp());
        while (lexer.lookCurrent(TokenType.OR)) {
            lexer.next();
            lAndExps.add(parseLAndExp());
        }
        Handler.pushOutput("<LOrExp>");
        return new LOrExp(lAndExps);
    }

    public ConstExp parseConstExp() {
        // ConstExp → AddExp [note: Ident used must be constant]
        Handler.pushOutput("<ConstExp>");
        return new ConstExp(parseAddExp());
    }
}


