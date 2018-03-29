package lexer;

/**
 * @Author: ny
 * @Date: Created in 15:39 2018/3/29 0029
 */
public class Token {
    public final String tag;
    public Token(String tag){
        this.tag = tag;
    }

    @Override
    public String toString() {
        return tag;
    }
}
