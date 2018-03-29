package lexer;

/**
 * @Author: ny
 * @Date: Created in 15:42 2018/3/29 0029
 * 操作符
 */
public class Operator extends Word {
    public Operator(String s, String tag){
        super(s,tag);
    }
    public static final Operator
        ADD = new Operator("+", Tag.ADD),
        DIFF = new Operator("-", Tag.DIFF),
        MUL = new Operator("*", Tag.MUL),
        DIV = new Operator("/", Tag.DIV),
        GT = new Operator(">", Tag.GT),
        LT = new Operator("<", Tag.LT),
        GE = new Operator(">=", Tag.GE),
        LE = new Operator("<=", Tag.LE),
        EQ = new Operator("==", Tag.EQ),
        NE = new Operator("!=", Tag.NE),
        NOT = new Operator("!", Tag.NOT);

    @Override
    public String toString() {
        return "operator:\t" + lexeme;
    }
}
