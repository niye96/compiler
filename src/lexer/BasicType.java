package lexer;

/**
 * @Author: ny
 * @Date: Created in 16:33 2018/3/29 0029
 * 基本数据类型
 */
public class BasicType extends Word{
    public BasicType(String s){
        super(s, Tag.BASIC);
    }
    public static final BasicType
        INT = new BasicType("int");
    @Override
    public String toString() {
        return "basicType:\t" + lexeme;
    }
}
