package parser;

import lexer.Lexer;

import java.util.*;

/**
 * @Author: ny
 * @Date: Created in 9:50 2018/4/5 0005
 */
public class Parser {
    private Grammar grammar;
    private Lexer lexer;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;
    public Parser(Lexer lexer, Grammar grammar){
        this.lexer = lexer;
        this.grammar = grammar;
        first = new HashMap<>();
        follow = new HashMap<>();
        init();
    }

    public void init(){
        getFirst();
        getFollow();
        output(this.first, "FIRST");
        System.out.println();
        output(this.follow, "FOLLOW");
    }
    public void getFirst(){
        // 非终止符号集合
        Set<String> non = grammar.nontermination;
        for(String key : non) {
            if(!first.containsKey(key)) {
                first.put(key, calFirst(key));
            }
        }
    }
    public Set<String> calFirst(String key){
        Set<String> result = new HashSet<>();
        List<List<Symbol>> right = grammar.grammar.get(key).right;
        //遍历所有产生式的表达式
        for(int i = 0, len1 = right.size(); i < len1; i++){
            for(int j = 0, len2 = right.get(i).size(); j < len2; j++){
                Symbol symbol = right.get(i).get(j);
                if(symbol.isTermination()){
                    result.add(symbol.str);
                    break;
                }else{
                    if(key.equals(symbol.str)) continue;
                    else{
                        //如果first集中以求解出，则直接使用，否则递归求解
                        Set<String> nextFirst = null;
                        if(first.containsKey(symbol.str)){
                            nextFirst = first.get(symbol.str);
                        }else{
                            nextFirst = calFirst(symbol.str);
                            // 将求出的结果放入first集中，防止重复计算
                            first.put(symbol.str, nextFirst);
                        }
                        // 如果包含空，则要去除空
                        if(nextFirst.contains("ε")){
                            //防止集合中已有空
                            Set<String> temp = new HashSet<>();
                            temp.addAll(nextFirst);
                            temp.remove("ε");
                            result.addAll(temp);
                            // 如果是非终止符号是最后一个，不用去空
                            if(j == len2 - 1)
                                result.add("ε");
                        }else{
                            result.addAll(nextFirst);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
    public void getFollow(){
        Set<String> nontermination = grammar.nontermination;
        for (String key : nontermination){
            if(!follow.containsKey(key)){
                follow.put(key, calFollow(key));
            }
        }
    }

    public Set<String> calFollow(String key){
        Set<String> result = new HashSet<>();
        result.add("#");
        // 遍历整个文法
        for(String k : grammar.nontermination) {
            Production p  = grammar.grammar.get(k);
            List<List<Symbol>> right = p.right;
            for (int i = 0, len1 = right.size(); i < len1; i++) {
                for (int j = 0, len2 = right.get(i).size(); j < len2; j++) {
                    Symbol symbol = right.get(i).get(j);
                    if(!symbol.isTermination() && key.equals(symbol.str)){
                        // 如果出现了S -> AaS' 的情况，将FOLLOW(S)加入FOLLOW(S')中
                        if(j == len2 - 1){
                            if(!p.left.str.equals(symbol.str)) {
                                Set<String> temp = null;
                                if (follow.containsKey(p.left.str)) {
                                    temp = follow.get(p.left.str);
                                } else {
                                    temp = calFollow(p.left.str);
                                    follow.put(p.left.str, temp);
                                }
                                result.addAll(temp);
                            }
                        }else{
                            // 如果是S -> AS'a或者AS'BC的情况
                            Symbol nextSymbol = right.get(i).get(j + 1);
                            if(nextSymbol.isTermination()){
                                result.add(nextSymbol.str);
                            }else{
                                // 邻接的非终止符号的first集合
                                Set<String> first = null;
                                do {
                                    first = this.first.get(nextSymbol.str);
                                    // 如果包含空，则FOLLOW(S') = {FIRST(B) - ε} U {FIRST(C) - ε} U FOLLOW(S)
                                    if(first.contains("ε")){
                                        Set<String> temp = new HashSet<>();
                                        temp.addAll(first);
                                        temp.remove("ε");
                                        result.addAll(temp);
                                        // 如果是S -> AS'BC AS'bc
                                        nextSymbol = right.get(i).get(j + 1);
                                        // 加入FOLLOW(S)
                                        if(j == len2 - 2){
                                            if(follow.containsKey(p.left.str)){
                                                temp = follow.get(p.left.str);
                                            }else{
                                                temp = calFollow(p.left.str);
                                                follow.put(p.left.str, temp);
                                            }
                                            result.addAll(temp);
                                        }
                                        j++;
                                    }else{
                                        result.addAll(first);
                                        break;
                                    }
                                }while(j < len2 - 1 && !symbol.isTermination());
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public void output(Map<String, Set<String>> map, String type){
        Set<Map.Entry<String,Set<String>>> entrySet = map.entrySet();
        for(Map.Entry<String,Set<String>> entry : entrySet) {
            System.out.print(type + "(" + entry.getKey() + ")={");
            Set<String> set = entry.getValue();
            List<String> list = new ArrayList<>(set);
            for(int i = 0, len = list.size(); i < len; i++){
                System.out.print(list.get(i));
                if(i != len - 1)
                    System.out.print(",");
            }
            System.out.println("}");
        }
    }
}
