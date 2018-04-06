package parser;

/**
 * @Author: ny
 * @Date: Created in 15:56 2018/4/5 0005
 */
public class Goto {
    public int from;
    public int to;
    public Symbol symbol;
    public Goto(int from, Symbol symbol, int to){
        this.from = from;
        this.to = to;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return from + ", "+symbol.str+", " + to;
    }
}
