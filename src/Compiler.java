import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args) {
        String filePath = "testfile.txt";

        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            String content = new String(fileBytes);

            Lexer lexer = new Lexer(content);
            ArrayList<Token> tokenList = Handler.getTokenList(lexer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
