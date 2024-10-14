package core;

import exception.*;
import lexicon.Token;
import lexicon.TokenType;
import semantics.Symbol;
import semantics.SymbolType;
import syntax.*;
import syntax.Character;
import syntax.Number;

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
                && lexer.lookAhead(TokenType.IDENFR) && !lexer.lookDoubleAhead(TokenType.LPARENT)) {
            decls.add(parseDecl());
        }
        while ((lexer.lookCurrent(TokenType.INTTK) || lexer.lookCurrent(TokenType.CHARTK)
                || lexer.lookCurrent(TokenType.VOIDTK)) && lexer.lookAhead(TokenType.IDENFR) && lexer.lookDoubleAhead(TokenType.LPARENT)) {
            funcDefs.add(parseFuncDef());
        }
        MainFuncDef mainFuncDef = parseMainFuncDef();

        CompUnit compUnit = new CompUnit(decls, funcDefs, mainFuncDef);
        // Handler.addSyntacticUnit(compUnit);
        return compUnit;
    }

    public Decl parseDecl() {
        // Decl → ConstDecl | VarDecl
        return lexer.lookCurrent(TokenType.CONSTTK) ? new Decl(parseConstDecl()) : new Decl(parseVarDecl());
    }

    public ConstDecl parseConstDecl() {
        // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';' // i
        if (!lexer.lookCurrent(TokenType.CONSTTK)) {
            throw new RuntimeException("Expect 'const', but get " + lexer.peek());
        }
        lexer.next();
        BType bType = parseBType();

        ArrayList<ConstDef> constDefs = new ArrayList<>();
        constDefs.add(parseConstDef(bType));
        while (lexer.lookCurrent(TokenType.COMMA)) {
            lexer.next();
            constDefs.add(parseConstDef(bType));
        }
        checkSemicolonAndPass();

        ConstDecl constDecl = new ConstDecl(bType, constDefs);

        // Handler.addSyntacticUnit(constDecl);
        return constDecl;
    }

    public BType parseBType() {
        // BType → 'int' | 'char'
        if (lexer.lookCurrent(TokenType.INTTK)) {
            lexer.next();
            return new BType("int");
        } else if (lexer.lookCurrent(TokenType.CHARTK)) {
            lexer.next();
            return new BType("char");
        } else {
            throw new RuntimeException("Expect 'int' or 'char', but get " + lexer.peek());
        }
    }

    public ConstDef parseConstDef(BType btype) {
        // ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal // k
        if (!lexer.lookCurrent(TokenType.IDENFR)) {
            throw new RuntimeException("Expect an identifier, but get " + lexer.peek());
        }
        String ident = lexer.peek().value();
        int lineNum = lexer.getLineNum();
        lexer.next();

        boolean isArray = false;
        ConstExp constExp = null;
        if (lexer.lookCurrent(TokenType.LBRACK)) {
            isArray = true;
            lexer.next();
            constExp = parseConstExp();
            checkRightBracketAndPass();
        }
        if (!lexer.lookCurrent(TokenType.ASSIGN)) {
            throw new RuntimeException("Expect '=', but get " + lexer.peek());
        }
        lexer.next();
        ConstInitVal constInitVal = parseConstInitVal();

        Symbol symbol = isArray ?
                Symbol.createConstArraySymbol(btype.isInt() ?
                        SymbolType.CONST_INT_ARRAY : SymbolType.CONST_CHAR_ARRAY, ident, SymbolManager.getScopeId(), constExp, constInitVal) :
                Symbol.createConstVarSymbol(btype.isInt() ? SymbolType.CONST_INT :
                        SymbolType.CONST_CHAR, ident, SymbolManager.getScopeId(), constInitVal);
        SymbolManager.registerSymbol(symbol, lineNum);

        ConstDef constDef = isArray ? new ConstDef(ident, constExp, constInitVal) : new ConstDef(ident, constInitVal);
        // Handler.addSyntacticUnit(constDef);
        return constDef;
    }

    public ConstInitVal parseConstInitVal() {
        // ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
        if (lexer.lookCurrent(TokenType.STRCON)) {
            String str = lexer.peek().value();
            lexer.next();
            ConstInitVal constInitVal = new ConstInitVal(str);
            // Handler.addSyntacticUnit(constInitVal);
            return constInitVal;
        } else if (lexer.lookCurrent(TokenType.LBRACE)) {
            lexer.next();
            if (lexer.lookCurrent(TokenType.RBRACE)) {
                lexer.next();
                ConstInitVal constInitVal = new ConstInitVal(new ArrayList<>());
                // Handler.addSyntacticUnit(constInitVal);
                return constInitVal;
            }
            ArrayList<ConstExp> constExps = new ArrayList<>();
            constExps.add(parseConstExp());
            while (lexer.lookCurrent(TokenType.COMMA)) {
                lexer.next();
                constExps.add(parseConstExp());
            }
            if (!lexer.lookCurrent(TokenType.RBRACE)) {
                throw new RuntimeException("Expect '}', but get " + lexer.peek());
            }
            lexer.next();
            ConstInitVal constInitVal = new ConstInitVal(constExps);
            // Handler.addSyntacticUnit(constInitVal);
            return constInitVal;
        } else {
            ConstExp constExp = parseConstExp();
            ConstInitVal constInitVal = new ConstInitVal(constExp);
            // Handler.addSyntacticUnit(constInitVal);
            return constInitVal;
        }
    }

    public VarDecl parseVarDecl() {
        // VarDecl → BType VarDef { ',' VarDef } ';' // i
        BType bType = parseBType();
        ArrayList<VarDef> varDefs = new ArrayList<>();
        varDefs.add(parseVarDef(bType));
        while (lexer.lookCurrent(TokenType.COMMA)) {
            lexer.next();
            varDefs.add(parseVarDef(bType));
        }
        checkSemicolonAndPass();

        VarDecl varDecl = new VarDecl(bType, varDefs);
        // Handler.addSyntacticUnit(varDecl);
        return varDecl;
    }

    public VarDef parseVarDef(BType bType) {
        // VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal // k
        if (!lexer.lookCurrent(TokenType.IDENFR)) {
            throw new RuntimeException("Expect an identifier, but get " + lexer.peek());
        }
        String ident = lexer.peek().value();
        int lineNum = lexer.getLineNum();
        lexer.next();

        ConstExp constExp = null;
        InitVal initVal = null;
        if (lexer.lookCurrent(TokenType.LBRACK)) {
            lexer.next();
            constExp = parseConstExp();
            checkRightBracketAndPass();
        }
        if (lexer.lookCurrent(TokenType.ASSIGN)) {
            lexer.next();
            initVal = parseInitVal();
        }
        VarDef varDef = new VarDef(ident, constExp, initVal);

        Symbol symbol = constExp != null ?
                Symbol.createArraySymbol(bType.isInt() ? SymbolType.INT_ARRAY : SymbolType.CHAR_ARRAY, ident, SymbolManager.getScopeId(), constExp, initVal) :
                Symbol.createVarSymbol(bType.isInt() ? SymbolType.INT : SymbolType.CHAR, ident, SymbolManager.getScopeId(), initVal);
        SymbolManager.registerSymbol(symbol, lineNum);
        // Handler.addSyntacticUnit(varDef);
        return varDef;
    }

    public InitVal parseInitVal() {
        // InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
        if (lexer.lookCurrent(TokenType.STRCON)) {
            String str = lexer.peek().value();
            lexer.next();
            InitVal initVal = new InitVal(str);
            // Handler.addSyntacticUnit(initVal);
            return initVal;
        } else if (lexer.lookCurrent(TokenType.LBRACE)) {
            lexer.next();
            if (lexer.lookCurrent(TokenType.RBRACE)) {
                lexer.next();
                InitVal initVal = new InitVal(new ArrayList<>());
                // Handler.addSyntacticUnit(initVal);
                return initVal;
            }
            ArrayList<Exp> exps = new ArrayList<>();
            exps.add(parseExp());
            while (lexer.lookCurrent(TokenType.COMMA)) {
                lexer.next();
                exps.add(parseExp());
            }
            if (!lexer.lookCurrent(TokenType.RBRACE)) {
                throw new RuntimeException("Expect '}', but get " + lexer.peek());
            }
            lexer.next();
            InitVal initVal = new InitVal(exps);
            // Handler.addSyntacticUnit(initVal);
            return initVal;
        } else {
            InitVal initVal = new InitVal(parseExp());
            // Handler.addSyntacticUnit(initVal);
            return initVal;
        }
    }

    public FuncDef parseFuncDef() {
        // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // j
        FuncType funcType = parseFuncType();
        if (!lexer.lookCurrent(TokenType.IDENFR)) {
            throw new RuntimeException("Expect an identifier, but get " + lexer.peek());
        }
        String funcName = lexer.peek().value();
        int lineNum = lexer.getLineNum();
        lexer.next();

        if (!lexer.lookCurrent(TokenType.LPARENT)) {
            throw new RuntimeException("Expect '(', but get " + lexer.peek());
        }
        lexer.next();

        FuncFParams funcFParams = new FuncFParams(new ArrayList<>());
        if (lexer.lookCurrent(TokenType.INTTK) || lexer.lookCurrent(TokenType.CHARTK)) {
            SymbolManager.startAddingFuncFParams();
            funcFParams = parseFuncFParams();
            SymbolManager.endAddingFuncFParams();
        }
        checkRightParenthesisAndPass();

        SymbolType type = funcType.isVoid() ? SymbolType.VOID_FUNC : funcType.isInt() ? SymbolType.INT_FUNC : SymbolType.CHAR_FUNC;
        Symbol symbol = Symbol.createFuncSymbol(type, funcName, SymbolManager.getScopeId(), funcFParams);
        SymbolManager.registerSymbol(symbol, lineNum);

        Block block = parseBlock(false, funcType.isVoid(), funcType.isInt() || funcType.isChar());
        FuncDef funcDef = new FuncDef(funcType, funcName, funcFParams, block);
        // Handler.addSyntacticUnit(funcDef);
        return funcDef;
    }

    public FuncType parseFuncType() {
        // FuncType → 'void' | 'int' | 'char'
        if (!lexer.lookCurrent(TokenType.INTTK) && !lexer.lookCurrent(TokenType.CHARTK) && !lexer.lookCurrent(TokenType.VOIDTK)) {
            throw new RuntimeException("Expect 'int', 'char', or 'void', but get " + lexer.peek());
        }
        FuncType funcType = new FuncType(lexer.lookCurrent(TokenType.INTTK) ? "int" : lexer.lookCurrent(TokenType.CHARTK) ? "char" : "void");
        lexer.next();
        // Handler.addSyntacticUnit(funcType);
        return funcType;
    }

    public MainFuncDef parseMainFuncDef() {
        // MainFuncDef → 'int' 'main' '(' ')' Block // j
        if (!lexer.lookCurrent(TokenType.INTTK)) {
            throw new RuntimeException("Expect 'int', but get " + lexer.peek());
        }
        lexer.next();
        if (!lexer.lookCurrent(TokenType.MAINTK)) {
            throw new RuntimeException("Expect 'main', but get " + lexer.peek());
        }

        lexer.next();
        if (!lexer.lookCurrent(TokenType.LPARENT)) {
            throw new RuntimeException("Expect '(', but get " + lexer.peek());
        }
        lexer.next();
        checkRightParenthesisAndPass();
        Block block = parseBlock(false, false, true);

        MainFuncDef mainFuncDef = new MainFuncDef(block);
        // Handler.addSyntacticUnit(mainFuncDef);
        return mainFuncDef;
    }

    public FuncFParams parseFuncFParams() {
        // FuncFParams → FuncFParam { ',' FuncFParam }
        ArrayList<FuncFParam> params = new ArrayList<>();

        params.add(parseFuncFParam());

        while (lexer.lookCurrent(TokenType.COMMA)) {
            lexer.next();
            params.add(parseFuncFParam());
        }
        FuncFParams funcFParams = new FuncFParams(params);
        // Handler.addSyntacticUnit(funcFParams);
        return funcFParams;
    }

    public FuncFParam parseFuncFParam() {
        // FuncFParam → BType Ident ['[' ']'] // k
        BType bType = parseBType();
        if (!lexer.lookCurrent(TokenType.IDENFR)) {
            throw new RuntimeException("Expect an identifier, but get " + lexer.peek());
        }
        int lineNum = lexer.getLineNum();
        String ident = lexer.peek().value();
        lexer.next();

        boolean isArray = false;
        if (lexer.lookCurrent(TokenType.LBRACK)) {
            isArray = true;
            lexer.next();
            checkRightBracketAndPass();
        }
        FuncFParam funcFParam = new FuncFParam(bType, ident, isArray);

        Symbol symbol = isArray ?
                Symbol.createArraySymbol(bType.isInt() ? SymbolType.INT_ARRAY : SymbolType.CHAR_ARRAY, ident, SymbolManager.getScopeId(), null, null) :
                Symbol.createVarSymbol(bType.isInt() ? SymbolType.INT : SymbolType.CHAR, ident, SymbolManager.getScopeId(), null);
        SymbolManager.registerSymbol(symbol, lineNum);
        // Handler.addSyntacticUnit(funcFParam);
        return funcFParam;
    }

    public Block parseBlock(boolean inLoop, boolean checkVoid, boolean checkReturn) {
        // Block → '{' { BlockItem } '}'
        SymbolManager.pushScope();
        lexer.next();

        ArrayList<BlockItem> blockItems = new ArrayList<>();
        while (!lexer.lookCurrent(TokenType.RBRACE)) {
            blockItems.add(parseBlockItem(inLoop, checkVoid));
        }
        int lineNum = lexer.getLineNum();
        lexer.next();
        Block block = new Block(blockItems);

        if (checkReturn) {
            try {
                if (blockItems.isEmpty() || !blockItems.get(blockItems.size() - 1).isReturnStmt()) {
                    throw new MissingReturnException("Missing return statement in function", lineNum);
                }
            } catch (MissingReturnException ignored) {
            }
        }
        // Handler.addSyntacticUnit(block);
        SymbolManager.popScope();
        return block;
    }

    public BlockItem parseBlockItem(boolean inLoop, boolean checkVoid) {
        // BlockItem → Decl | Stmt
        if (lexer.lookCurrent(TokenType.CONSTTK) || (lexer.lookCurrent(TokenType.INTTK) || lexer.lookCurrent(TokenType.CHARTK))) {
            Decl decl = parseDecl();
            return new BlockItem(decl);
        } else {
            Stmt stmt = parseStmt(inLoop, checkVoid);
            return new BlockItem(stmt);
        }
    }

    private Stmt parseStmtIf(boolean inLoop, boolean checkVoid) {
        ArrayList<Unit> units = new ArrayList<>();
        lexer.next();
        if (!lexer.lookCurrent(TokenType.LPARENT)) {
            throw new RuntimeException("Expect '(', but get " + lexer.peek());
        }
        lexer.next();
        Cond cond = parseCond();
        units.add(cond);
        checkRightParenthesisAndPass();
        Stmt thenStmt = parseStmt(inLoop, checkVoid);
        units.add(thenStmt);

        if (lexer.lookCurrent(TokenType.ELSETK)) {
            lexer.next();
            Stmt elseStmt = parseStmt(inLoop, checkVoid);
            units.add(elseStmt);
        }
        Stmt stmt = new Stmt(StmtType.IF, units);
        // Handler.addSyntacticUnit(stmt);
        return stmt;
    }

    private Stmt parseStmtFor(boolean checkVoid) {
        ArrayList<Unit> units = new ArrayList<>();
        lexer.next();
        if (!lexer.lookCurrent(TokenType.LPARENT)) {
            throw new RuntimeException("Expect '(', but get " + lexer.peek());
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
                throw new RuntimeException("Expect ';', but get " + lexer.peek());
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
                throw new RuntimeException("Expect ';', but get " + lexer.peek());
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
            checkRightParenthesisAndPass();
        }
        Stmt stmt = parseStmt(true, checkVoid);
        units.add(stmt);
        Stmt forStmt = new Stmt(StmtType.FOR, units);
        // Handler.addSyntacticUnit(forStmt);
        return forStmt;
    }

    private Stmt parseStmtPrintf() {
        ArrayList<Unit> units = new ArrayList<>();
        int lineNum = lexer.getLineNum();

        lexer.next();
        if (!lexer.lookCurrent(TokenType.LPARENT)) {
            throw new RuntimeException("Expect '(', but get " + lexer.peek());
        }
        lexer.next();
        if (!lexer.lookCurrent(TokenType.STRCON)) {
            throw new RuntimeException("Expect a string constant, but get " + lexer.peek());
        }
        units.add(lexer.peek());
        lexer.next();

        while (lexer.lookCurrent(TokenType.COMMA)) {
            lexer.next();
            units.add(parseExp());
        }
        checkRightParenthesisAndPass();
        checkSemicolonAndPass();
        Stmt stmt = new Stmt(StmtType.PRINTF, units);

        try {
            int argNum = ((Token) (units.get(0))).value().split("%[cd]").length - 1;
            if (argNum != units.size() - 1) {
                throw new PrintFormatMismatchException("The number of arguments does not match the format string", lexer.getLineNum());
            }
        } catch (PrintFormatMismatchException ignored) {
        }
        // Handler.addSyntacticUnit(stmt);
        return stmt;
    }

    private Stmt parseStmtOther() {
        /* ASSIGN? EXPR? GETINT? GETCHAR? */
        ArrayList<Unit> units = new ArrayList<>();
        if (lexer.lookCurrent(TokenType.PLUS) || lexer.lookCurrent(TokenType.MINU) || lexer.lookCurrent(TokenType.NOT) || // UnaryOp->UnaryExp->EXPR
                lexer.lookCurrent(TokenType.INTCON) || lexer.lookCurrent(TokenType.CHRCON) || lexer.lookCurrent(TokenType.LPARENT) || // Number, Character,(->PrimaryExp->EXPR
                lexer.lookCurrent(TokenType.IDENFR) && lexer.lookAhead(TokenType.LPARENT) // UnaryExp->PrimaryExp->LVal->Ident->EXPR
        ) {
            // EXPR, definitely
            units.add(parseExp());
            try {
                checkSemicolonAndPass();
                Stmt stmt = new Stmt(StmtType.EXPR, units);
                // Handler.addSyntacticUnit(stmt);
                return stmt;
            } catch (RuntimeException e) {
                return new Stmt(StmtType.EXPR, units);
            }
        }

        try { // EXPR?
            lexer.save();
            Handler.save();
            units.add(parseExp());// Possible Exception Here
            if (!lexer.lookCurrent(TokenType.SEMICN)) {
                throw new MissingSemicolonException("Expect ';', but get " + lexer.peek(), lexer.getLastToken().lineNum());
            }
            lexer.next();
            Stmt stmt = new Stmt(StmtType.EXPR, units);
            // Handler.addSyntacticUnit(stmt);
            return stmt;
        } catch (Exception e) { // NOT EXPR, starts with LVal!
            lexer.restore();
            Handler.restore();
            units.clear();

            String ident = lexer.peek().value();
            int lineNum = lexer.getLineNum();
            LVal lVal = parseLVal();
            Symbol symbol = SymbolManager.useSymbol(ident, lineNum);
            try {
                if (symbol != null && symbol.isConstType()) {
                    throw new ImmutableConstantException("Trying to assign to a constant variable", lineNum);
                }
            } catch (ImmutableConstantException ignored) {
            }
            units.add(lVal);
            if (!lexer.lookCurrent(TokenType.ASSIGN)) {
                throw new RuntimeException("Expect '=', but get " + lexer.peek());
            }
            lexer.next();
            if (lexer.lookCurrent(TokenType.GETINTTK) || lexer.lookCurrent(TokenType.GETCHARTK)) { // GETINT+GETCHAR
                StmtType stmtType = lexer.lookCurrent(TokenType.GETINTTK) ? StmtType.GETINT : StmtType.GETCHAR;
                lexer.next();
                if (!lexer.lookCurrent(TokenType.LPARENT)) {
                    throw new RuntimeException("Expect '(', but get " + lexer.peek());
                }
                lexer.next();
                checkRightParenthesisAndPass();
                checkSemicolonAndPass();
                Stmt stmt = new Stmt(stmtType, units);
                // Handler.addSyntacticUnit(stmt);
                return stmt;
            } else { // ASSIGN
                units.add(parseExp());
                checkSemicolonAndPass();
                Stmt stmt = new Stmt(StmtType.ASSIGN, units);
                // Handler.addSyntacticUnit(stmt);
                return stmt;
            }
        }
    }


    private Stmt parseStmtBlock(boolean inLoop, boolean checkVoid) {
        Block block = parseBlock(inLoop, checkVoid, false);
        Stmt stmt = new Stmt(StmtType.BLOCK, new ArrayList<>() {{
            add(block);
        }});
        // Handler.addSyntacticUnit(stmt);
        return stmt;
    }

    private Stmt parseStmtReturn(boolean checkVoid) {
        int lineNum = lexer.getLineNum();
        lexer.next();
        if (lexer.lookCurrent(TokenType.SEMICN)) {
            lexer.next();
            Stmt stmt = new Stmt(StmtType.RETURN);
            // Handler.addSyntacticUnit(stmt);
            return stmt;
        } else {
            try {
                if (checkVoid) {
                    throw new ReturnInVoidFunctionException("Return statement in void function", lineNum);
                }
            } catch (ReturnInVoidFunctionException ignored) {
            }
            ArrayList<Unit> units = new ArrayList<>();
            units.add(parseExp());
            checkSemicolonAndPass();
            Stmt stmt = new Stmt(StmtType.RETURN, units);
            // Handler.addSyntacticUnit(stmt);
            return stmt;
        }
    }

    private Stmt parseStmtBreak(boolean inLoop) {
        try {
            if (!inLoop) {
                throw new BreakContinueOutsideLoopException("Break statement outside loop", lexer.getLineNum());
            }
        } catch (BreakContinueOutsideLoopException ignored) {
        }
        lexer.next();
        checkSemicolonAndPass();
        Stmt stmt = new Stmt(StmtType.BREAK);
        // Handler.addSyntacticUnit(stmt);
        return stmt;
    }

    private Stmt parseStmtContinue(boolean inLoop) {
        try {
            if (!inLoop) {
                throw new BreakContinueOutsideLoopException("Continue statement outside loop", lexer.getLineNum());
            }
        } catch (BreakContinueOutsideLoopException ignored) {
        }
        lexer.next();
        checkSemicolonAndPass();
        Stmt stmt = new Stmt(StmtType.CONTINUE);
        // Handler.addSyntacticUnit(stmt);
        return stmt;
    }

    private void checkSemicolonAndPass() {
        try {
            if (!lexer.lookCurrent(TokenType.SEMICN)) {
                throw new MissingSemicolonException("Expect ';', but get " + lexer.peek(), lexer.getLastToken().lineNum());
            }
            lexer.next();
        } catch (MissingSemicolonException ignored) {
        }
    }

    private Stmt parseStmtEmpty() {
        lexer.next();
        Stmt stmt = new Stmt(StmtType.EMPTY);
        // Handler.addSyntacticUnit(stmt);
        return stmt;
    }


    public Stmt parseStmt(boolean inLoop, boolean checkVoid) {
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
        return switch (lexer.peek().tokenType()) {
            case LBRACE -> parseStmtBlock(inLoop, checkVoid); // BLOCK
            case IFTK -> parseStmtIf(inLoop, checkVoid); // IF
            case FORTK -> parseStmtFor(checkVoid); // FOR
            case BREAKTK -> parseStmtBreak(inLoop); // BREAK
            case CONTINUETK -> parseStmtContinue(inLoop); // CONTINUE
            case RETURNTK -> parseStmtReturn(checkVoid); // RETURN
            case PRINTFTK -> parseStmtPrintf(); // PRINTF
            case SEMICN -> parseStmtEmpty(); // EMPTY
            default -> parseStmtOther(); // ASSIGN? EXPR? GETINT? GETCHAR?
        };
    }

    public ForStmt parseForStmt() {
        // ForStmt → LVal '=' Exp
        int lineNum = lexer.getLineNum();
        String ident = lexer.peek().value();
        LVal lVal = parseLVal();
        if (!lexer.lookCurrent(TokenType.ASSIGN)) {
            throw new RuntimeException("Expect '=', but get " + lexer.peek());
        }
        lexer.next();
        Exp exp = parseExp();
        ForStmt forStmt = new ForStmt(lVal, exp);
        // Handler.addSyntacticUnit(forStmt);
        return forStmt;
    }

    public Exp parseExp() {
        // Exp → AddExp
        AddExp addExp = parseAddExp();
        Exp exp = new Exp(addExp);
        // Handler.addSyntacticUnit(exp);
        return exp;
    }

    public Cond parseCond() {
        // Cond → LOrExp
        LOrExp lOrExp = parseLOrExp();
        Cond cond = new Cond(lOrExp);
        // Handler.addSyntacticUnit(cond);
        return cond;
    }

    public LVal parseLVal() {
        // LVal → Ident ['[' Exp ']'] // k
        String ident = lexer.peek().value();

        lexer.next();

        Exp exp = null;
        if (lexer.lookCurrent(TokenType.LBRACK)) {
            lexer.next();
            exp = parseExp();
            checkRightBracketAndPass();
        }
        LVal lVal = new LVal(ident, exp);
        // Handler.addSyntacticUnit(lVal);
        return lVal;
    }

    private void checkRightBracketAndPass() {
        try {
            if (!lexer.lookCurrent(TokenType.RBRACK)) {
                throw new MissingRightBracketException("Expect ']', but get " + lexer.peek(), lexer.getLastToken().lineNum());
            }
            lexer.next();
        } catch (MissingRightBracketException ignored) {
        }
    }

    public PrimaryExp parsePrimaryExp() {
        // PrimaryExp → '(' Exp ')' | LVal | Number | Character // j
        if (lexer.lookCurrent(TokenType.LPARENT)) {
            lexer.next();
            Exp exp = parseExp();
            checkRightParenthesisAndPass();
            PrimaryExp primaryExp = new PrimaryExp(exp);
            // Handler.addSyntacticUnit(primaryExp);
            return primaryExp;
        } else if (lexer.lookCurrent(TokenType.INTCON)) {
            Number number = parseNumber();
            PrimaryExp primaryExp = new PrimaryExp(number);
            // Handler.addSyntacticUnit(primaryExp);
            return primaryExp;
        } else if (lexer.lookCurrent(TokenType.CHRCON)) {
            Character character = parseCharacter();
            PrimaryExp primaryExp = new PrimaryExp(character);
            // Handler.addSyntacticUnit(primaryExp);
            return primaryExp;
        } else {
            LVal lVal = parseLVal();
            PrimaryExp primaryExp = new PrimaryExp(lVal);
            // Handler.addSyntacticUnit(primaryExp);
            return primaryExp;
        }
    }

    public Number parseNumber() {
        if (lexer.lookCurrent(TokenType.INTCON)) {
            String intConst = lexer.peek().value();
            lexer.next();
            Number number = new Number(intConst);
            // Handler.addSyntacticUnit(number);
            return number;
        } else {
            throw new RuntimeException("Expect an integer constant, but get " + lexer.peek());
        }
    }

    public Character parseCharacter() {
        if (lexer.lookCurrent(TokenType.CHRCON)) {
            char charConst = lexer.peek().value().charAt(0);
            lexer.next();
            Character character = new Character(charConst);
            // Handler.addSyntacticUnit(character);
            return character;
        } else {
            throw new RuntimeException("Expect a character constant, but get " + lexer.peek());
        }
    }

    public UnaryExp parseUnaryExp() {
        // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // j
        if (lexer.lookCurrent(TokenType.IDENFR) && lexer.lookAhead(TokenType.LPARENT)) {
            int lineNum = lexer.getLineNum();
            String ident = lexer.peek().value();
            Symbol symbol = SymbolManager.useSymbol(ident, lineNum);

            lexer.next();
            lexer.next();

            FuncRParams funcRParams = null;
            if (lexer.lookCurrent(TokenType.PLUS) || lexer.lookCurrent(TokenType.MINU) || lexer.lookCurrent(TokenType.NOT) ||
                    lexer.lookCurrent(TokenType.INTCON) || lexer.lookCurrent(TokenType.CHRCON) || lexer.lookCurrent(TokenType.LPARENT) ||
                    lexer.lookCurrent(TokenType.IDENFR)) {
                funcRParams = parseFuncRParams();
            }
            int funcRParamCount = funcRParams == null ? 0 : funcRParams.getSize();
            int funcFParamCount = symbol == null ? funcRParamCount : symbol.getFuncFParams().getSize();
            try {
                if (symbol == null || funcRParamCount != funcFParamCount) {
                    throw new ArgumentCountMismatchException("The number of arguments does not match the number of parameters", lineNum);
                }
                if (funcRParams != null && !funcRParams.checkType(symbol.getFuncFParams())) {
                    throw new ArgumentTypeMismatchException("The type of arguments does not match the type of parameters", lineNum);
                }
            } catch (ArgumentCountMismatchException | ArgumentTypeMismatchException ignored) {
            }
            checkRightParenthesisAndPass();
            UnaryExp unaryExp = new UnaryExp(ident, funcRParams);
            // Handler.addSyntacticUnit(unaryExp);
            return unaryExp;
        } else if (lexer.lookCurrent(TokenType.PLUS) || lexer.lookCurrent(TokenType.MINU) || lexer.lookCurrent(TokenType.NOT)) {
            UnaryOp unaryOp = parseUnaryOp();
            UnaryExp unaryExp = parseUnaryExp();
            UnaryExp result = new UnaryExp(unaryOp, unaryExp);
            // Handler.addSyntacticUnit(result);
            return result;
        } else {
            PrimaryExp primaryExp = parsePrimaryExp();
            UnaryExp unaryExp = new UnaryExp(primaryExp);
            // Handler.addSyntacticUnit(unaryExp);
            return unaryExp;
        }
    }

    public FuncRParams parseFuncRParams() {
        // FuncRParams → Exp { ',' Exp }
        ArrayList<Exp> exps = new ArrayList<>();
        ArrayList<SymbolType> types = new ArrayList<>();

        types.add(lexer.GuessExpSymbolType());
        exps.add(parseExp());
        while (lexer.lookCurrent(TokenType.COMMA)) {
            lexer.next();
            types.add(lexer.GuessExpSymbolType());
            exps.add(parseExp());
        }
        FuncRParams funcRParams = new FuncRParams(exps, types);
        // Handler.addSyntacticUnit(funcRParams);
        return funcRParams;
    }

    private void checkRightParenthesisAndPass() {
        try {
            if (!lexer.lookCurrent(TokenType.RPARENT)) {
                throw new MissingRightParenthesisException("Expect ')', but get " + lexer.peek(), lexer.getLastToken().lineNum());
            }
            lexer.next();
        } catch (MissingRightParenthesisException ignored) {
        }
    }

    public UnaryOp parseUnaryOp() {
        // UnaryOp → '+' | '−' | '!'
        if (lexer.lookCurrent(TokenType.PLUS) || lexer.lookCurrent(TokenType.MINU) || lexer.lookCurrent(TokenType.NOT)) {
            lexer.next();
            UnaryOp unaryOp = new UnaryOp(lexer.peek().value());
            // Handler.addSyntacticUnit(unaryOp);
            return unaryOp;
        } else {
            throw new RuntimeException("Expect '+', '-', or '!', but get " + lexer.peek());
        }
    }

    public MulExp parseMulExp() {
        // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
        ArrayList<UnaryExp> unaryExps = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();

        unaryExps.add(parseUnaryExp());
        while (lexer.lookCurrent(TokenType.MULT) || lexer.lookCurrent(TokenType.DIV) || lexer.lookCurrent(TokenType.MOD)) {
            ops.add(lexer.peek());
            // Handler.addSyntacticUnit(new MulExp(unaryExps, ops)); // Add intermediate MulExp for each op
            lexer.next();
            unaryExps.add(parseUnaryExp());
        }
        MulExp mulExp = new MulExp(unaryExps, ops);
        // Handler.addSyntacticUnit(mulExp);
        return mulExp;
    }

    public AddExp parseAddExp() {
        // AddExp → MulExp | AddExp ('+' | '−') MulExp
        ArrayList<MulExp> mulExps = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();

        mulExps.add(parseMulExp());
        while (lexer.lookCurrent(TokenType.PLUS) || lexer.lookCurrent(TokenType.MINU)) {
            ops.add(lexer.peek());
            // Handler.addSyntacticUnit(new AddExp(mulExps, ops)); // Add intermediate AddExp for each op
            lexer.next();
            mulExps.add(parseMulExp());
        }
        AddExp addExp = new AddExp(mulExps, ops);
        // Handler.addSyntacticUnit(addExp);
        return addExp;
    }

    public RelExp parseRelExp() {
        // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
        ArrayList<AddExp> addExps = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();

        addExps.add(parseAddExp());
        while (lexer.lookCurrent(TokenType.LSS) || lexer.lookCurrent(TokenType.LEQ) || lexer.lookCurrent(TokenType.GRE)
                || lexer.lookCurrent(TokenType.GEQ)) {
            ops.add(lexer.peek());
            // Handler.addSyntacticUnit(new RelExp(addExps, ops)); // Add intermediate RelExp for each op
            lexer.next();
            addExps.add(parseAddExp());
        }
        RelExp relExp = new RelExp(addExps, ops);
        // Handler.addSyntacticUnit(relExp);
        return relExp;
    }

    public EqExp parseEqExp() {
        // EqExp → RelExp | EqExp ('==' | '!=') RelExp
        ArrayList<RelExp> relExps = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();

        relExps.add(parseRelExp());
        while (lexer.lookCurrent(TokenType.EQL) || lexer.lookCurrent(TokenType.NEQ)) {
            ops.add(lexer.peek());
            // Handler.addSyntacticUnit(new EqExp(relExps, ops)); // Add intermediate EqExp for each op
            lexer.next();
            relExps.add(parseRelExp());
        }
        EqExp eqExp = new EqExp(relExps, ops);
        // Handler.addSyntacticUnit(eqExp);
        return eqExp;
    }

    public LAndExp parseLAndExp() {
        // LAndExp → EqExp | LAndExp '&&' EqExp
        ArrayList<EqExp> eqExps = new ArrayList<>();
        eqExps.add(parseEqExp());
        while (lexer.lookCurrent(TokenType.AND)) {
            // Handler.addSyntacticUnit(new LAndExp(eqExps)); // Add intermediate LAndExp for each op
            lexer.next();
            eqExps.add(parseEqExp());
        }
        LAndExp lAndExp = new LAndExp(eqExps);
        // Handler.addSyntacticUnit(lAndExp);
        return lAndExp;
    }

    public LOrExp parseLOrExp() {
        // LOrExp → LAndExp | LOrExp '||' LAndExp
        ArrayList<LAndExp> lAndExps = new ArrayList<>();
        lAndExps.add(parseLAndExp());
        while (lexer.lookCurrent(TokenType.OR)) {
            // Handler.addSyntacticUnit(new LOrExp(lAndExps)); // Add intermediate LOrExp for each op
            lexer.next();
            lAndExps.add(parseLAndExp());
        }
        LOrExp lOrExp = new LOrExp(lAndExps);
        // Handler.addSyntacticUnit(lOrExp);
        return lOrExp;
    }

    public ConstExp parseConstExp() {
        // ConstExp → AddExp [note: Ident used must be constant]
        AddExp addExp = parseAddExp();
        ConstExp constExp = new ConstExp(addExp);
        // Handler.addSyntacticUnit(constExp);
        return constExp;
    }

}


