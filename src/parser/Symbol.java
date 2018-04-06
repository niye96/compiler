package parser;

/**
 * @Author: ny
 * @Date: Created in 21:32 2018/4/4 0004
 */
public class Symbol {
    public static final int NONTERMINATION = 1;
    public static final int TERMINATION = 0;

    public String str = null;
    public int type;
    public Symbol(String s, int type){
        str = s;
        this.type = type;
    }
    public boolean isTermination(){
        return this.type == TERMINATION;
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Symbol){
            return str.equals(((Symbol) obj).str) && type == ((Symbol) obj).type;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return type;
    }
}
