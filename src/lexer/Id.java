package lexer;

/**
 * @Author: ny
 * @Date: Created in 15:41 2018/3/29 0029
 * 标识符
 */
public class Id extends Word{
    public Id(String s){
        super(s, Tag.ID);
    }

    @Override
    public String toString() {
        return "id:\t" + lexeme;
    }
}
