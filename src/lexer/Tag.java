package lexer;

/**
 * @Author: ny
 * @Date: Created in 15:36 2018/3/29 0029
 */
public class Tag {
    public final static String
        // 关键词
        IF = "IF", THEN = "THEN", ELSE = "ELSE", WHILE = "WHILE", DO = "DO",
        // 操作符
        ADD = "ADD", DIFF = "DIFF", MUL = "MUL", DIV = "DIV", GT = "GT", LT = "LT",
        EQ = "EQ", GE = "GE", LE = "LE", NE = "NE", NOT = "NOT",
        // 基础数据类型
        BASIC = "BASIC",
        // 标识符
        ID = "ID",
        // 分隔符
        ASSIGN = "ASSIGN", SLP = "SLP"/*左括号*/, SRP = "SRP"/*右括号*/, SEMI = "SEMI"/*分号*/,
        // 数字
        NUM = "NUM",
        // 出错处理
        ERROR = "ERROR";

}
