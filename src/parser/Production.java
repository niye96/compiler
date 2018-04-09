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
    public boolean existNull;

    public Production(String left){
        this.left = new Symbol(left, Symbol.NONTERMINATION);
        this.right = new ArrayList<>();
    }
    public void add(List<Symbol> symbols){
        right.add(symbols);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(left.toString() + "->");
        for(int i = 0; i < right.size(); i++){
            for(int j = 0; j < right.get(i).size(); j++){
                buffer.append(right.get(i).get(j).toString() + " ");
            }
            if(i < right.size() - 1)
                buffer.append("|");
        }
        return buffer.toString();
    }
    public boolean existNull(){
        return existNull;
    }
    public void setExistNull(boolean existNull){
        this.existNull = existNull;
    }
}
