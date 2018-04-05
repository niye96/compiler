package parser;

import lexer.Lexer;

import java.io.*;
import java.util.*;

/**
 * @Author: ny
 * @Date: Created in 22:00 2018/4/3 0003
 * 文法分析类
 */
public class Grammar {
    public Map<String, Production> grammar;
    public Set<String> termination;
    public Set<String> nontermination;
    public Symbol begin;

    public Grammar(File file, String begin){
        grammar = new HashMap<>();
        termination = new HashSet<>();
        nontermination = new HashSet<>();
        readFile(file);
        this.begin =  grammar.get(begin).left;
    }

    public void readFile(File file){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = br.readLine()) != null){
                String[] s1 = line.split("→");
                String[] s2 = s1[1].split("\\|");
                s1[0] = s1[0].trim();
                nontermination.add(s1[0]);
                for(int i = 0; i < s2.length; i++) {
                    if(!"".equals(s2[i].trim())){
                        Production production = new Production(s1[0]);
                        if(grammar.containsKey(s1[0])){
                            grammar.get(s1[0]).add(resolve(s2[i]));
                        }else{
                            production.add(resolve(s2[i]));
                            grammar.put(s1[0], production);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("解析错误");
        }finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public List<Symbol> resolve(String str){
        List<Symbol> right = new ArrayList<>();
        for(int i = 0, len = str.length(); i < len; i++){
            Symbol symbol = null;
            char c= str.charAt(i);
            if(c <= 'Z' && c >= 'A'){
                symbol = new Symbol(String.valueOf(c), Symbol.NONTERMINATION);
            }else if(c <= 'z' && c >= 'a'){
                StringBuffer buffer = new StringBuffer();
                buffer.append(c);
                while(i + 1 < len && !isBlank(str.charAt(i + 1))){
                    buffer.append(str.charAt(++i));
                }
                symbol = new Symbol(buffer.toString(), Symbol.TERMINATION);
            }else if(isBlank(c)){
                continue;
            } else {
                switch (c){
                    case '=':
                        if(i + 1 < len && str.charAt(i + 1) == '=')
                            symbol = new Symbol("==", Symbol.TERMINATION);
                        else
                            symbol = new Symbol("=", Symbol.TERMINATION);
                        break;
                    default:
                        symbol = new Symbol(String.valueOf(c), Symbol.TERMINATION);
                        break;
                }
            }
            if(symbol != null){
                right.add(symbol);
                if(symbol.isTermination()){
                    termination.add(symbol.str);
                }else{
                    nontermination.add(symbol.str);
                }
            }
        }
        return right;
    }

    public boolean isBlank(char c){
        return c == '\t' || c == '\n' || c == ' ';
    }
}
