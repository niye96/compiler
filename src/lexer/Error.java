package lexer;

/**
 * @Author: ny
 * @Date: Created in 19:24 2018/3/29 0029
 * 出错处理
 */
public class Error extends Token{
    public final String msg;
    public final int line;
    public Error(String msg, int line){
        super(Tag.ERROR);
        this.msg = msg;
        this.line = line;
    }

    @Override
    public String toString() {
        return "error in "+ line +" line :\t" + msg;
    }
}
