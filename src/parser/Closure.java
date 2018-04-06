package parser;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Author: ny
 * @Date: Created in 15:56 2018/4/5 0005
 */
public class Closure {
    Grammar grammar;
    List<Node> origin;
    // 通过原始的Closure计算出来的
    List<Node> cal;
    // 方便计算goto
    Map<Symbol, List<Node>> gotoMap;
    public Closure(Grammar grammar){
        this.grammar = grammar;
        origin = new ArrayList<>();
        cal = new ArrayList<>();
        gotoMap = new HashMap<>();
    }

    public void put(Symbol left, List<Symbol> right, int pos){
        Node node = null;
        if(pos < right.size()) {
            Symbol symbol = right.get(pos);
            // 如果产生式为ε，则化为空
            if(right.size() == 1 && "ε".equals(symbol.str)){
                node = new Node(left, new ArrayList<>(), 0);
            }else{
                node = new Node(left, right, pos);
                if (!gotoMap.containsKey(symbol)) {
                    List<Node> nodes = new ArrayList<>();
                    nodes.add(node);
                    gotoMap.put(symbol, nodes);
                } else {
                    gotoMap.get(symbol).add(node);
                }
            }
            this.origin.add(node);
        }else if(pos == right.size()){
            node = new Node(left, right, pos);
            this.origin.add(node);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Closure)
            return origin.equals(((Closure) obj).origin);
        return false;
    }

    @Override
    public int hashCode() {
        return origin.hashCode();
    }

    public void calc(){
        int pre = cal.size();
        Queue<String> queue = new LinkedBlockingDeque<>();
        Set<String> used = new HashSet<>();
        Set<Node> existInOrigin = new HashSet<>();
        for(int i = 0, len = origin.size(); i < len; i++){
            Node node = origin.get(i);
            existInOrigin.add(node);
            // 产生式右部的字符如果未到末尾并且为非终止符号
            if(node.pos < node.right.size()) {
                Symbol symbol = node.right.get(node.pos);
                if(!symbol.isTermination() && !used.contains(symbol.str)) {
                    queue.add(symbol.str);
                    // 使用过的产生式首部加入集合中
                    used.add(symbol.str);
                }
            }
        }

        while(!queue.isEmpty()){
            String itemName = queue.poll();
            used.add(itemName);
            Production nextp = grammar.grammar.get(itemName);
            List<List<Symbol>> nextRights = nextp.right;
            for(int i = 0, len = nextRights.size(); i < len; i++){
                Symbol symbol = nextRights.get(i).get(0);
                Node node = null;
                // 如果产生式右部为空，则用空的list代替
                if("ε".equals(symbol.str)){
                    node = new Node(nextp.left,new ArrayList<>());
                }else {
                    // 排除已经计算过的字符
                    node = new Node(nextp.left, nextRights.get(i));
                    if (!symbol.isTermination() && !used.contains(symbol.str)) {
                        queue.add(symbol.str);
                        used.add(symbol.str);
                    }
                }
                // 如果origin中已存在的产生式，不往cal里添加
                if(!existInOrigin.contains(node))
                    this.cal.add(node);
                if(!gotoMap.containsKey(symbol)){
                    List<Node> nodes = new ArrayList<>();
                    nodes.add(node);
                    gotoMap.put(symbol,nodes);
                }else{
                    gotoMap.get(symbol).add(node);
                }
            }
        }
    }
    class Node{
        Symbol left;
        List<Symbol> right;
        int pos;
        Node(Symbol left, List<Symbol> right, int pos){
            this.left = left;
            this.right = right;
            this.pos = pos;
        }
        Node(Symbol left, List<Symbol> right){
            this(left, right, 0);
        }

        @Override
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(left.toString() + " -> ");
            for(int i = 0; i < right.size(); i++){
                if(i == pos)
                    buffer.append("·");
                buffer.append(right.get(i).toString()+" ");
            }
            if(right.size() == pos)
                buffer.append("·");
            return buffer.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Node){
                return ((Node) obj).left.equals(left) && ((Node) obj).pos == pos && ((Node) obj).right.equals(right);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return left.hashCode() + right.hashCode() + pos;
        }
    }

    public static void main(String[] args) {
//        Symbol s1 = new Symbol("E", 1);
//        Symbol s2 = new Symbol("E", 1);
//        List<Symbol> symbols1 = new ArrayList<>();
//        List<Symbol> symbols2 = new ArrayList<>();
//        Node node1 = new Node(s1,symbols1);
//        Node node2 = new Node(s2,symbols2);
//        symbols1.add(s1);
//        symbols2.add(s2);
//        System.out.println(s1.equals(s2));
//        System.out.println(symbols1.equals(symbols2));
//        System.out.println(node1.equals(node2));
//        System.out.println(node1.hashCode() == node2.hashCode());
    }
}
