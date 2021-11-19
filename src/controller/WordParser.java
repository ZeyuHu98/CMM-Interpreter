package controller;

import classes.CodeError;
import classes.Symbol;
import classes.SymbolType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hzy on 2018/10/30.
 * 词法分析程序
 */
public class WordParser {
    //定义error变量
    private static CodeError error3 = new CodeError(3, "AnnotationError");
    private static CodeError error4 = new CodeError(4, "CodeError");
    //成员变量
    private List<Symbol> symbols = new ArrayList<>();//返回的symbol数组
    private List<char[]> codes;//存储输入的代码，一个char[]存储一行

    //用于词法分析的变量

    private int line = 0;//存储行号
    private int lineNum = 1;//存储当前字符位置
    private int lineCount = 0;//存储当前行生成的symbol个数
    private char currentChar = '\0';//存储当前字符


    //构造词法分析对象
    public WordParser(List<char[]> codes) {
        this.codes = codes;
    }

    //读取字符函数
    private void readChar() {
        currentChar = codes.get(line)[lineNum];
        lineNum++;
    }

    //换行函数
    private boolean changeLine() {
        line++;
        if (line < codes.size()) {
            lineCount = 0;
            lineNum = 0;
            readChar();
            return true;
        } else {
            return false;
        }
    }

    //词法分析函数
    public List<Symbol> parseCode() {
        this.symbols = new ArrayList<>();
        currentChar = codes.get(line)[0];
        L1:
        while (true) {
            switch (getSymbol()) {
                case 1://可继续执行的情况
                    continue L1;
                case 0://出现codeError的情况
                    continue L1;
                case -1://换行
                    if (changeLine()) {
                        continue L1;
                    } else {
                        return symbols;
                    }
                case -2://出现多行注释的情况
                    while (line < codes.size()) {
                        L2:
                        for (int i = 0; i < codes.get(line).length - 1; i++) {
                            if (codes.get(line)[i] == '*') {
                                if (codes.get(line)[i + 1] == '/') {
                                    if (codes.get(line)[i + 2] != '\n') {
                                        lineNum = i + 2;
                                        readChar();
                                    } else if (changeLine()) {
                                        continue L1;
                                    } else {
                                        return symbols;
                                    }
                                } else {
                                    continue L2;
                                }
                            }
                        }
                        line++;
                    }
                    String str = "第" + line + "行: " + error3.toString();
                    System.out.println(str);
                    return symbols;
            }
        }
    }


    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
    }

    private int getSymbol() {
        StringBuilder currentChars = new StringBuilder();//存储在生成一个新的symbol前已经读取的字符
        while (currentChar == ' ' || currentChar == '\t') {//跳过空格与制表符
            readChar();
        }
        if (isLetter(currentChar) || currentChar == '_') {
            while (isLetter(currentChar) || isDigit(currentChar) || currentChar == '_') {
                currentChars.append(currentChar);
                readChar();
            }
            if (SymbolType.reserveWords.containsKey(currentChars.toString())) {//判断保留字
                Symbol symbol = new Symbol(SymbolType.reserveWords.get(currentChars.toString()), "保留字",
                        currentChars.toString(), line + 1, lineCount + 1);//生成保留字symbol
                symbols.add(symbol);
                lineCount++;
                currentChars.delete(0, currentChars.length());
                return 1;
            } else {//确定为标识符
                Symbol symbol = new Symbol(SymbolType.getIDENTIFIER(), "标识符",
                        currentChars.toString(), line + 1, lineCount + 1);//生成标识符symbol
                symbols.add(symbol);
                lineCount++;
                currentChars.delete(0, currentChars.length());
                return 1;
            }
        } else if (isDigit(currentChar)) {//判断是否为常量
            if (currentChar == '0') {//若为0，可能是0.xxxxx的情况或为常数0
                currentChars.append(currentChar);
                readChar();
                if (currentChar == '.') {//判断是否有小数点
                    currentChars.append(currentChar);
                    readChar();
                    if (isDigit(currentChar)) {
                        while (isDigit(currentChar)) {
                            currentChars.append(currentChar);
                            readChar();
                        }
                        Symbol symbol = new Symbol(SymbolType.getDOUBLEVAR(), "实数", currentChars.toString(),
                                line + 1, lineCount + 1);//生成0.xx常量symbol
                        symbols.add(symbol);
                        lineCount++;
                        currentChars.delete(0, currentChars.length());
                        return 1;
                    } else {
                        String str = "第" + (line+1) + "行: " + error4.toString();
                        System.out.println(str);
                        changeLine();
                        return 0;
                    }
                } else if (!isDigit(currentChar)) {//为常数零的情况
                    Symbol symbol = new Symbol(SymbolType.getINTVAR(), "整数", currentChars.toString(),
                            line + 1, lineCount + 1);
                    symbols.add(symbol);
                    currentChars.delete(0, currentChars.length());
                    lineCount++;
                    return 1;
                } else {//为0xx的情况，报错
                    String str = "第" + (line+1) + "行: " + error4.toString();
                    System.out.println(str);
                    changeLine();
                    return 0;
                }
            } else {//首位不为零，可能为整数，也可能为浮点数
                while (isDigit(currentChar)) {
                    currentChars.append(currentChar);
                    readChar();
                }
                if (currentChar == '.') {
                    currentChars.append(currentChar);
                    readChar();
                    if (isDigit(currentChar)) {
                        while (isDigit(currentChar)) {
                            currentChars.append(currentChar);
                            readChar();
                        }
                        Symbol symbol = new Symbol(SymbolType.getDOUBLEVAR(), "实数", currentChars.toString(),
                                line + 1, lineCount + 1);//生成xxx.xxx浮点数symbol
                        symbols.add(symbol);
                        lineCount++;
                        currentChars.delete(0, currentChars.length());
                    } else {//产生xxxx.错误
                        String str = "第" + (line+1) + "行: " + error4.toString();
                        System.out.println(str);
                        return 0;
                    }
                } else {
                    Symbol symbol = new Symbol(SymbolType.getINTVAR(), "整数", currentChars.toString(),
                            line + 1, lineCount + 1);//生成整形常量
                    symbols.add(symbol);
                    lineCount++;
                    currentChars.delete(0, currentChars.length());
                    return 1;
                }
            }
        } else if (currentChar == '.') {//·开头的情况
            String str = "第" + (line+1) + "行: " + error4.toString();
            System.out.println(str);
            changeLine();
            return 0;
        } else if (currentChar == '/') {//判断注释
            readChar();
            if (currentChar == '/') {
                return -1;
            } else if (currentChar == '*') {//处理多行注释
                for (int i = lineNum; i < codes.get(line).length - 1; i++) {
                    if (codes.get(line)[i] == '*' && codes.get(line)[i + 1] == '/') {
                        lineNum = i + 1;
                        return 1;
                    }
                }
                line++;
                return -2;
            } else {//error3
                Symbol symbol = new Symbol(SymbolType.operators.get("/"), "运算符", "/",
                        line + 1, lineCount + 1);
                symbols.add(symbol);
                lineCount++;
                return 1;
            }
//        } else if (currentChar == '&') {//判断关系运算符&&
//            readChar();
//            if (currentChar == '&') {
//                Symbol symbol = new Symbol(SymbolType.operators.get("&&"), "运算符", "&&",
//                        line + 1, lineCount + 1);
//                symbols.add(symbol);
//                lineCount++;
//                readChar();
//                return 1;
//            } else {
//                String str = "第" + line + "行: " + error4.toString();
//                System.out.println(str);
//                return 0;
//            }
//        } else if (currentChar == '|') {//判断关系运算符||
//            readChar();
//            if (currentChar == '|') {
//                Symbol symbol = new Symbol(SymbolType.operators.get("||"), "运算符", "||",
//                        line + 1, lineCount + 1);
//                symbols.add(symbol);
//                lineCount++;
//                readChar();
//                return 1;
//            } else {
//                String str = "第" + line + "行: " + error4.toString();
//                System.out.println(str);
//                return 0;
//            }
        } else if (SymbolType.operators.containsKey(String.valueOf(currentChar))) {//判断是否属于运算符
            Symbol symbol = null;
            switch (currentChar) {
                case '+':
                case '-':
                case '*':
                case '(':
                case ')':
                case '%':
                    symbol = new Symbol(SymbolType.operators.get(String.valueOf(currentChar)), "运算符", String.valueOf(currentChar),
                            line + 1, lineCount + 1);
                    readChar();
                    break;
                case '=':
                    readChar();
                    if (currentChar == '=') {
                        symbol = new Symbol(SymbolType.operators.get("=="), "运算符", "==",
                                line + 1, lineCount + 1);
                        readChar();//置下一判断值
                    } else {
                        symbol = new Symbol(SymbolType.operators.get("="), "运算符", "=",
                                line + 1, lineCount + 1);
                    }
                    break;
                case '>':
                    readChar();
                    if (currentChar == '=') {
                        symbol = new Symbol(SymbolType.operators.get(">="), "运算符", ">=",
                                line + 1, lineCount + 1);
                        readChar();//置下一判断值
                    } else {
                        symbol = new Symbol(SymbolType.operators.get(">"), "运算符", ">",
                                line + 1, lineCount + 1);
                    }
                    break;
                case '<':
                    readChar();
                    if (currentChar == '=') {
                        symbol = new Symbol(SymbolType.operators.get("<="), "运算符", "<=",
                                line + 1, lineCount + 1);
                        readChar();//置下一判断值
                    } else {
                        symbol = new Symbol(SymbolType.operators.get("<"), "运算符", "<",
                                line + 1, lineCount + 1);
                    }
                    break;
                case '!':
                    readChar();
                    if (currentChar == '=') {
                        symbol = new Symbol(SymbolType.operators.get("!="), "运算符", "!=",
                                line + 1, lineCount + 1);
                        readChar();//置下一判断值
                    } else {
                        String str = "第" + line + "行: " + error4.toString();
                    }
                    break;
            }
            symbols.add(symbol);
            lineCount++;
            return 1;
        } else if (SymbolType.delimiters.containsKey(String.valueOf(currentChar))) {
            Symbol symbol = new Symbol(SymbolType.delimiters.get(String.valueOf(currentChar)), "界符", String.valueOf(currentChar),
                    line + 1, lineCount + 1);
            symbols.add(symbol);
            lineCount++;
            readChar();
            return 1;
        } else if (currentChar == '\n') {
            return -1;
        }else{
            String str = "第" + (line+1) + "行: " + error4.toString();
            System.out.println(str);
            readChar();
            return 0;
        }
        return 0;
    }
}
