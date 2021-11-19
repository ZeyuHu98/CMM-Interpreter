package classes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hzy on 2018/11/1.
 * symbol类型
 */
public class SymbolType {
    //运算符
    public static final int PLUS = 1;// +
    public static final int MINUS = 2;// -
    public static final int MULTIPLY = 3;// *
    public static final int DIVIDE = 4;// /
    public static final int ASSIGN = 5;// =
    public static final int GREATER = 6;// >
    public static final int GOE = 7;// >=
    public static final int LESS = 8;// <
    public static final int LOE = 9;// <=
    public static final int EQUAL = 10;// ==
    public static final int NEQUAL = 11;// !=
    public static final int MOD = 12;// %
//    public static final int OR = 13;// ||
    public static final int LPAR = 14;// (
    public static final int RPAR = 15;// )
    //界符
    public static final int LBRA = 16;// [
    public static final int RBRA = 17;// ]
    public static final int LBRACE = 18;// {
    public static final int RBRACE = 19;// }
    public static final int COMMA = 20;// ,
    public static final int SEMI = 21;// ;
    //保留字
    public static final int WRITE = 22;// write
    public static final int IF = 23;// if
    public static final int ELSE = 24;// else
    public static final int WHILE = 25;// while
    public static final int INT = 26;// int
    public static final int DOUBLE = 27;// double
    public static final int CONTINUE = 28;// continue
    //注释
    public static final int OLANNO = 29;// //
    public static final int LANNO = 30;// /*
    public static final int RANNO = 31;// *?
    //常量
    public static final int INTNUM = 32;// int类型常量
    public static final int DOUBLENUM = 33;// double类型常量
    //标识符
    public static final int IDENTIFIER = 34;

    public static final Map<String, Integer> operators= new HashMap<>();
    static {
        operators.put("+",PLUS);
        operators.put("-",MINUS);
        operators.put("*",MULTIPLY);
        operators.put("/",DIVIDE);
        operators.put("=",ASSIGN);
        operators.put(">",GREATER);
        operators.put(">=",GOE);
        operators.put("<",LESS);
        operators.put("<=",LOE);
        operators.put("==",EQUAL);
        operators.put("!=",NEQUAL);
        operators.put("%",MOD);
//        operators.put("&&",AND);
//        operators.put("||",OR);
        operators.put("(",LPAR);
        operators.put(")",RPAR);
    }

    public static final Map<String, Integer> delimiters = new HashMap<>();
    static {
        delimiters.put("[",LBRA);
        delimiters.put("]",RBRA);
        delimiters.put("{",LBRACE);
        delimiters.put("}",RBRACE);
        delimiters.put(",",COMMA);
        delimiters.put(";",SEMI);
    }

    public static final Map<String, Integer> reserveWords = new HashMap<>();
    static{
        reserveWords.put("if",IF);
        reserveWords.put("else",ELSE);
        reserveWords.put("while",WHILE);
        reserveWords.put("int",INT);
        reserveWords.put("double",DOUBLE);
        reserveWords.put("continue",CONTINUE);
        reserveWords.put("write",WRITE);
    }

    public static final Map<String, Integer> annotations = new HashMap<>();
    static{
        annotations.put("//",OLANNO);
        annotations.put("/*",LANNO);
        annotations.put("*/",RANNO);
    }

    public static int getINTVAR() {
        return INTNUM;
    }

    public static int getDOUBLEVAR() {
        return DOUBLENUM;
    }

    public static int getIDENTIFIER(){
        return IDENTIFIER;
    }
}
