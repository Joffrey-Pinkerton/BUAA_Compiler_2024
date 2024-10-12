import lexicon.Token;

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
            Parser parser = new Parser(lexer);
            parser.parseCompUnit();
            Handler.print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
