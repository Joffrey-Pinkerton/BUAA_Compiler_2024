import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        String filePath = "source.txt";

        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            String content = new String(fileBytes);

            Lexer lexer = new Lexer(content);

            ArrayList<Token> tokenList = new Handler(lexer).getTokenList();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
