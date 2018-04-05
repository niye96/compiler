package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ny
 * @Date: Created in 21:36 2018/4/4 0004
 */
public class Production {
    public Symbol left;
    public List<List<Symbol>> right;
    public int pos = 0;
    public Production(String left){
        this.left = new Symbol(left, Symbol.NONTERMINATION);
        this.right = new ArrayList<>();
    }
    public void add(List<Symbol> symbols){
        right.add(symbols);
    }


}
