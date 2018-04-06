package parser;

import lexer.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Author: ny
 * @Date: Created in 9:50 2018/4/5 0005
 */
public class Parser {
    private Grammar grammar;
    private Lexer lexer;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;
    private List<Closure> closures;
    private List<Goto> gotos;
    private Map<String, Object>[] LRTable;

    public Parser(Lexer lexer, Grammar grammar){
        this.lexer = lexer;
        this.grammar = grammar;
        first = new HashMap<>();
        follow = new HashMap<>();
        closures = new ArrayList<>();
        gotos = new ArrayList<>();
        init();
    }

    public void parse(){
        Stack<Integer> stack = new Stack<>();
        Stack<String> tokens = new Stack<>();
        stack.push(0);
        String now = null;
        try {
            now = scan();
            while (true){
                // 为空则开始读入字符，否则进行规约
                Object obj = LRTable[stack.lastElement()].get(now);
                if(obj == null || obj instanceof Integer) {
                    tokens.push(now);
                    stack.push((Integer) LRTable[stack.lastElement()].get(now));
                    now = scan();
                }else{
                    // 开始规约
                    int times = (int)((Object[])obj)[1];
                    String token = (String)((Object[])obj)[0];
                    for(int i = 0; i < times; i++){
                        tokens.pop();
                        stack.pop();
                    }
                    tokens.push(token);
                    stack.push((Integer) LRTable[stack.lastElement()].get(token));
                }
                // 输出栈和符号栈情况
                System.out.print("(");
                for(int i = 0; i < stack.size(); i++){
                    System.out.print(stack.get(i) + " ");
                }
                System.out.print(")     ");
                System.out.print("(");
                for(int i = 0; i < tokens.size(); i++){
                    System.out.print(tokens.get(i) + " ");
                }
                System.out.println(")");
            }
        }catch (Exception e){
            now = "$";
            Object result = LRTable[stack.lastElement()].get(now);
            if("acc".equals(result)){
                System.out.println("匹配成功");
            }else{
                System.out.println("匹配失败");
            }
        }
    }
    public String scan(){
        String now;
        Token token = null;
        try {
            token = lexer.scan();
            if (token.tag == Tag.ID) {
                now = "id";
            }else if (token.tag == Tag.NUM) {
                now = "int10";
            }else{
                now = ((Word)token).lexeme;
            }
        } catch (Exception e) {
            now = "$";
        }
        return now;
    }
    public void init(){
        // 计算first集合
        getFirst();
        // 计算follow集合
        getFollow();
        output(this.first, "FIRST");
        System.out.println();
        output(this.follow, "FOLLOW");
        // 计算closure和goto
        calc();
        outputGoto();
        // 生成LR表
        calLRTable();
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
        result.add("$");
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

    public void calc(){
        // 用于判别状态转移后的新状态是否已经存在
        Map<Closure, Integer> oldStatus = new HashMap<>();
        // 将开始符号的产生式加入Closure集合中
        Closure closure = new Closure(grammar);
        Production begin = grammar.grammar.get(grammar.begin);
        for(int i = 0, len = begin.right.size(); i < len; i++){
            closure.put(begin.left, begin.right.get(i), 0);
        }
        closure.calc();
        this.closures.add(closure);
        oldStatus.put(closure, 0);
        outputClosure(0);//test
        // 开始从第一个初始化的closure集合算出其他的closure集合
        Queue<Integer> queue = new LinkedBlockingDeque<>();
        Map<Symbol, List<Closure.Node>> gotoMap = null;
        queue.add(0);
        while(!queue.isEmpty()){
            int i = queue.poll();
            Closure tempClosure = this.closures.get(i);
            if(!tempClosure.gotoMap.isEmpty()){
                gotoMap = tempClosure.gotoMap;
                Set<Map.Entry<Symbol, List<Closure.Node>>> entrySet = gotoMap.entrySet();
                for(Map.Entry<Symbol, List<Closure.Node>> entry : entrySet){
                    Symbol key = entry.getKey();
                    if("ε".equals(key.str)) continue;
                    List<Closure.Node> nodes = entry.getValue();
                    Closure newClosure = new Closure(grammar);
                    for(int j = 0, len = nodes.size(); j < len; j++){
                        Closure.Node node = nodes.get(j);
                        newClosure.put(node.left, node.right, node.pos + 1);
                    }
                    // 如果状态已存在则不需要重新计算
                    Integer status = oldStatus.get(newClosure);
                    if(status != null){
                        this.gotos.add(new Goto(i, key, status));
                    }else {
                        newClosure.calc();
                        this.closures.add(newClosure);
                        int index = this.closures.size() - 1;
                        queue.add(index);
                        this.gotos.add(new Goto(i, key,index));
                        outputClosure(index);//test
                        oldStatus.put(newClosure,index);
                    }
                }
            }
        }
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
    public void outputClosure(int pos){
        Closure closure = this.closures.get(pos);
        System.out.println("\n\n第"+pos+"个状态");
        List<Closure.Node> temp = new ArrayList<>();
        temp.addAll(closure.origin);
        temp.addAll(closure.cal);
        for(int i = 0; i < temp.size(); i++){
            Closure.Node node = temp.get(i);
            System.out.print(node.left);
            System.out.print(" → ");
            List<Symbol> symbols = temp.get(i).right;
            for(int j = 0; j < symbols.size(); j++){
                if(j == node.pos)
                    System.out.print("·");
                System.out.print(symbols.get(j) + " ");
            }
            if(node.pos == symbols.size()) System.out.print("·");
            System.out.println();
        }
    }
    public void outputGoto(){
        System.out.println("\n\n*******");
        for(int i = 0; i < gotos.size(); i++){
            System.out.println(gotos.get(i));
        }
    }

    public void calLRTable(){
        LRTable = new HashMap[closures.size()];
        for(int i = 0; i < LRTable.length; i++){
            LRTable[i] = new HashMap<>();
        }
        for(int i = 0, len = gotos.size(); i < len; i++){
            Goto go = gotos.get(i);
            LRTable[go.from].put(go.symbol.str, go.to);
        }
        for(int i = 0, len1 = closures.size(); i < len1; i++) {
            Closure closure = closures.get(i);
            // 如果cal中包含有产生式右部为空的式子，则也可以进行规约
            List<Closure.Node> hasNull = closure.gotoMap.get(new Symbol("ε",Symbol.TERMINATION));
            List<Closure.Node> temp = null;
            if (hasNull == null && !closure.cal.isEmpty()) continue;
            if (hasNull != null) {
                temp = hasNull;
            } else {
                temp = closure.origin;
            }
            for (int j = 0, len2 = temp.size(); j < len2; j++) {
                Closure.Node node = temp.get(j);
                // 可归约的话，先看看产生式左部符号的follow集合
                if (node.pos == node.right.size()) {
                    Set<String> follow = this.follow.get(node.left.str);
                    for (String key : follow) {
                        if (node.left.str.equals(grammar.begin)) {
                            LRTable[i].put(key, "acc");
                        } else {
                            LRTable[i].put(key, new Object[]{node.left.str, node.right.size()});
                        }
                    }
                }
            }
        }
    }
}
