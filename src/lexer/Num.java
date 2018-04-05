package lexer;

/**
 * @Author: ny
 * @Date: Created in 15:41 2018/3/29 0029
 * 常数
 */
public class Num extends Token{
    public final int value;
    public Num(int v){
        super(Tag.NUM);
        value = v;
    }

    @Override
    public String toString() {
        return "(NUM,"+value+")";
    }
}
