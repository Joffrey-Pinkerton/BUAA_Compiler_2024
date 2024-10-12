import lexicon.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Handler {
    private static final Stack<String> outputStack = new Stack<>();
    private static final ArrayList<String> errorList = new ArrayList<>();

    public static ArrayList<Token> getTokenList(Lexer lexer) {
        ArrayList<Token> tokens = new ArrayList<>();

        while (lexer.notEnd()) {
            lexer.next();
            Token curToken = lexer.peek();
            if (curToken == null) {
                break;
            }
            tokens.add(curToken);
        }

        if (errorList.isEmpty()) {
            printInfo(tokens);
        } else {
            printErrors(errorList);
        }

        return tokens;
    }

    public static void pushOutput(String str) {
        outputStack.push(str);
    }

    public static void popOutput() {
        outputStack.pop();
    }

    public static void addErrorInfo(String error) {
        errorList.add(error);
    }


    public static ArrayList<String> getOutput(Parser parser) {
        ArrayList<String> output = new ArrayList<>();

        parser.parseCompUnit();

        return output;
    }


    private static void printInfo(ArrayList<Token> tokenList) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("lexer.txt"));
            for (Token token : tokenList) {
                bw.write(token.getType() + " " + token + "\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printErrors(ArrayList<String> errors) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("error.txt"));
            for (String error : errors) {
                bw.write(error + "\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
