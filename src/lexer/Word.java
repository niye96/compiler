package lexer;

/**
 * @Author: ny
 * @Date: Created in 15:41 2018/3/29 0029
 * 关键字
 */
public class Word extends Token {
    public String lexeme;

    public Word(String s, int tag) {
        super(tag);
        lexeme = s;
    }

    @Override
    public String toString() {
        return lexeme;
    }

}
