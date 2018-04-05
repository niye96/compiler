package lexer;

/**
 * @Author: ny
 * @Date: Created in 16:21 2018/3/29 0029
 */
public class Keyword extends Word{
    public Keyword(String s, String tag){
        super(s, tag);
    }
    public static final Keyword
        IF = new Keyword("if", Tag.IF),
        ELSE = new Keyword("else", Tag.ELSE),
        THEN = new Keyword("then", Tag.THEN),
        WHILE = new Keyword("while", Tag.WHILE),
        DO = new Keyword("do", Tag.DO);

    @Override
    public String toString() {
        return "(" + tag +",_)";
    }
}
