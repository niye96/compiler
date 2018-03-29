package lexer;

/**
 * @Author: ny
 * @Date: Created in 15:39 2018/3/29 0029
 */
public class Token {
    public final int tag;
    public Token(int tag){
        this.tag = tag;
    }

    @Override
    public String toString() {
        return ""+(char)tag;
    }
}
