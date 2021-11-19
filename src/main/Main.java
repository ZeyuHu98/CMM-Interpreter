package main;

import classes.Symbol;
import classes.SymbolNode;
import controller.CodeScanner;
import controller.GrammarParser;
import controller.MeaningParser;
import controller.WordParser;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Hzy on 2018/10/30.
 */
public class Main {

    //定义代码数组
    private static List<char[]> codes;
    private static List<Symbol> symbols;
    private static LinkedList<SymbolNode> symbolNodes;

    //声明编译器所用实例
    private static CodeScanner codeScanner = new CodeScanner();
    private static WordParser wordParser;
    private static GrammarParser grammarParser;
    private static MeaningParser meaningParser;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input your fileName:");
        //String filePath = scanner.nextLine();
        String filePath = "D:\\test.txt";
        codes = codeScanner.scanCode(filePath);
        //词法分析，获取symbols
        wordParser = new WordParser(codes);
        symbols = wordParser.parseCode();
        System.out.println("*************************************************\r\n词法分析：");
        for (Symbol symbol : symbols) {
            System.out.println(symbol);
        }
        //语法分析，获取treeNodes
        System.out.println("*************************************************\r\n语法分析：");
        grammarParser = new GrammarParser(symbols);
        symbolNodes = grammarParser.parseCode();
        for (SymbolNode symbolNode : symbolNodes) {
            System.out.println(symbolNode);
        }
        //语义分析，
        System.out.println("*************************************************\r\n语义分析（运行结果）：");
        meaningParser = new MeaningParser(symbolNodes);
        meaningParser.parseMeaning();
        meaningParser.getOutput();
    }
}
