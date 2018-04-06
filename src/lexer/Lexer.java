package lexer;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: ny
 * @Date: Created in 16:46 2018/3/29 0029
 */
public class Lexer {
    public Map<String, Word> tokens = new HashMap<>();
    public Map<String, Id> words = new HashMap<>();
    public StringBuffer buffer;
    // 缓冲区指针
    public int pos = -1;
    // 当前行
    public int line = 0;
    // 当前字符
    char now = ' ';
    public Lexer() {
        // 添加关键字
        try {
            Class keywordClazz = Class.forName("lexer.Keyword");
            Field[] fields = keywordClazz.getDeclaredFields();
            for(Field field : fields) {
                Object obj = field.get(keywordClazz);
                if(obj instanceof Word)
                    initMap((Word)obj);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 添加分隔符
        try {
            Class separatorClazz = Class.forName("lexer.Separator");
            Field[] fields = separatorClazz.getDeclaredFields();
            for(Field field : fields) {
                Object obj = field.get(separatorClazz);
                if(obj instanceof Word)
                    initMap((Word)obj);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        initMap(BasicType.INT);
        initMap(Operator.ADD);
        initMap(Operator.DIFF);
        initMap(Operator.MUL);
        initMap(Operator.DIV);
    }

    void initMap(Word word) {
        tokens.put(word.lexeme, word);
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
                try {
                    nextChar();
                }catch (Exception e){
                    // 读到文件末尾不做处理
                    now = ' ';
                }
            }while(Character.isLetterOrDigit(now));
            String w = word.toString();
            // 判断是否为关键字
            Word found = tokens.get(w);
            if (found == null) {
                Id id = new Id(w);
//                tokens.put(Tag.ID, id);
                words.put(id.lexeme, id);
                return id;
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
            if (Character.isLetter(now) || now == '{' || now == '('){
                StringBuffer errorId = new StringBuffer();
                errorId.append(num);
                do{
                    errorId.append(now);
                    nextChar();
                } while(Character.isLetterOrDigit(now));
                return new Error("非法标识符 " + errorId.toString(),line);
            }
            return new Num(num);
        }
        //判断是否为运算符或分隔符
        if (now == '>') {
            nextChar();
            if(now == '=') {
                nextChar();
                return Operator.GE;
            }
            else return Operator.GT;
        } else if (now == '<') {
            nextChar();
            if(now == '='){
                nextChar();
                return Operator.LE;
            }
            else return Operator.LT;
        } else if (now == '=') {
            nextChar();
            if(now == '=') {
                nextChar();
                return Operator.EQ;
            }
            return Separator.ASSIGN;
        } else if (now == '!') {
            if(now == '=') {
                nextChar();
                return Operator.NE;
            }
            else return Operator.NOT;
        }

        char temp = now;
        now = ' ';
        if(tokens.containsKey(String.valueOf(temp))){
            return tokens.get(String.valueOf(temp));
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
        System.out.println("\n\n***********token表**************");
        printMap(tokens);
        System.out.println("\n\n***********符号表**************");
        printMap(words);
    }

    public void printMap(Map map){
        Set<Map.Entry> entrySet = map.entrySet();
        for(Map.Entry entry : entrySet){
            Object val = entry.getValue();
            if(val instanceof Word){
                System.out.println("("+((Word) val).lexeme+","+"_)");

            }
        }

    }
}
