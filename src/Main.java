import lexer.Lexer;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        lexer.readFile(new File("F:\\compiler\\src\\1.txt"));
        lexer.output();
    }
}
