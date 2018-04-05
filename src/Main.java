import lexer.Lexer;
import parser.Grammar;
import parser.Parser;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        lexer.readFile(new File("F:\\compiler\\src\\1.txt"));
//        lexer.output();
        Parser parser = new Parser(lexer, new Grammar(new File("F:\\compiler\\src\\2.txt"), "E"));
    }
}
