package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Compiler {
    public static void main(String[] args) {
        String filePath = "testfile.txt";

        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            String content = new String(fileBytes);
            Lexer lexer = new Lexer(content);
            SymbolManager symbolManager = new SymbolManager();
            Parser parser = new Parser(lexer, symbolManager);
            parser.parseCompUnit();
            Handler.print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
