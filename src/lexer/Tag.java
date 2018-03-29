package lexer;

/**
 * @Author: ny
 * @Date: Created in 15:36 2018/3/29 0029
 */
public class Tag {
    public final static int
        // 关键词
        IF = 256, THEN = 257, ELSE = 258, WHILE = 259, DO = 260,
        // 操作符
        ADD = 261, DIFF = 262, MUL = 263, DIV = 264, GT = 265, LT = 266,
        EQ = 267, GE = 268, LE = 269, NE = 270, NOT = 271,
        // 基础数据类型
        BASIC = 272,
        // 标识符
        ID = 273,
        // 分隔符
        ASSIGN = 274, SLP = 275/*左括号*/, SRP = 276/*右括号*/, SEMI = 277/*分号*/,
        // 数字
        NUM = 278,
        // 出错处理
        ERROR = 279;

}
