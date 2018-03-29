package lexer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ny
 * @Date: Created in 16:46 2018/3/29 0029
 */
public class Lexer {
    public static Map<String, Word> keyword = null;
    public StringBuffer buffer;
    // 缓冲区指针
    public int pos = -1;
    // 当前行
    public int line = 0;
    // 当前字符
    char now;
    public Lexer() {
        if (keyword == null) {
            keyword = new HashMap();
            initMap(Keyword.IF);
            initMap(Keyword.THEN);
            initMap(Keyword.ELSE);
            initMap(Keyword.WHILE);
            initMap(Keyword.DO);
            initMap(BasicType.INT);
            initMap(Separator.SEMI);
            initMap(Separator.SLP);
            initMap(Separator.SRP);
            initMap(Operator.ADD);
            initMap(Operator.DIFF);
            initMap(Operator.MUL);
            initMap(Operator.DIV);
        }
    }

    void initMap(Word word) {
        keyword.put(word.lexeme, word);
    }

    public void readFile(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            buffer.deleteCharAt(buffer.length() - 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    public Token scan() throws Exception{
        // 去除空格换行制表符
        while (true) {
            if (now == ' ' || now == '\t');
            else if (now == '\n') line++;
            else break;
            nextChar();
        }
        // 判断是否为word
        if (Character.isLetter(now)) {
            StringBuffer word = new StringBuffer();
            do{
                word.append(now);
                nextChar();
            }while(Character.isLetterOrDigit(now));
            String w = word.toString();
            // 判断是否为关键字
            Word found = keyword.get(w);
            if (found == null) {
                return new Id(w);
            } else {
                return found;
            }
        }
        //判断是否为数字
        if (Character.isDigit(now)) {
            int num = 0;
            do {
                num *= 10;
                num += Character.digit(now, 10);
                nextChar();
            } while (Character.isDigit(now));
            // 判断数字后面如果是字符，则出错
            if (Character.isLetter(now) || now == '{' || now == '(')
                return new Error("非法标识符",line);
            return new Num(num);
        }
        //判断是否为运算符或分隔符
        if (now == '>') {
            nextChar();
            if(now == '=') return Operator.GE;
            else return Operator.GT;
        } else if (now == '<') {
            nextChar();
            if(now == '=') return Operator.LE;
            else return Operator.LT;
        } else if (now == '=') {
            nextChar();
            if(now == '=') return Operator.EQ;
            return Separator.ASSIGN;
        } else if (now == '!') {
            if(now == '=') return Operator.NE;
            else return Operator.NOT;
        }

        char temp = now;
        now = ' ';
        if(keyword.containsKey(String.valueOf(temp))){
            return keyword.get(String.valueOf(temp));
        }
        return new Token(String.valueOf(temp));
    }

    void nextChar() throws Exception{
        if(pos >= buffer.length()){
            throw new Exception("读到文件末尾");
        }else{
            now = buffer.charAt(++pos);
        }
    }

    public void output(){
        try{
            while(true){
                System.out.println(scan());
            }
        }catch (Exception e){
            System.out.println("结束");
        }
    }
}
