import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Handler {
    private final Lexer lexer;

    Handler(Lexer lexer) {
        this.lexer = lexer;
    }

    public ArrayList<Token> getTokenList() {
        ArrayList<Token> tokens = new ArrayList<>();

        while (lexer.notEnd()) {
            lexer.next();

            Token curToken = lexer.getCurrentToken();
            if (curToken == null) {
                break;
            }
            tokens.add(curToken);
        }

        if (lexer.getErrorLog().isEmpty()) {
            printInfo(tokens);
        } else {
            printErrors(lexer.getErrorLog());
        }

        return tokens;
    }

    private void printInfo(ArrayList<Token> tokenList) {
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

    private void printErrors(ArrayList<String> errors) {
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
