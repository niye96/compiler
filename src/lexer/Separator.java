package lexer;

/**
 * @Author: ny
 * @Date: Created in 15:42 2018/3/29 0029
 * 分隔符
 */
public class Separator extends Word {
    public Separator(String s, String tag) {
        super(s, tag);
    }

    public static final Separator
        ASSIGN = new Separator("=", Tag.ASSIGN),
        SLP = new Separator("(", Tag.SLP),
        SRP = new Separator(")", Tag.SRP),
        SEMI = new Separator(";", Tag.SEMI);

    @Override
    public String toString() {
        return "("+ tag + ",_)";
    }
}
