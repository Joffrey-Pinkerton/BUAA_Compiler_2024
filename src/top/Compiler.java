package top;

import syntax.Unit;
import syntax.UnitType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Compiler {
    public static void main(String[] args) {
        String filePath = "testfile.txt";

        for(UnitType type:UnitType.values()){
            System.out.println(type);
        }

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
