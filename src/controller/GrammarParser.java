package controller;

import classes.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Hzy on 2018/11/26.
 * 语法分析程序
 * CMM LL(1)文法
 * program -> stmt-sequence
 * stmt-sequence -> statement ; stmt-sequence | statement | ε
 * statement -> if-stmt | while-stmt | assign-stmt | declare-stmt | CONTINUE;
 * stmt-block -> statement | { stmt-sequence }
 * if-stmt -> IF ( judge-exp ) stmt-block | IF ( judge-exp ) stmt-block ELSE stmt-block
 * while-stmt -> WHILE ( judge-exp ) stmt-block
 * assign-stmt -> IDENTIFIER = exp;
 * declare-stmt -> (INT | DOUBLE) INDENTIFIER [= exp ];
 * judge-exp -> exp logical-op exp
 * exp -> term add-op exp | term
 * term -> factor mul-op term | factor
 * factor -> ( exp ) | NUMBER | IDENTIFIER
 * logical-op -> > | < | >= | <= | == | || | && | !=
 * add-op -> + | -
 * mul-op -> * | /
 */
public class GrammarParser {
    private List<Symbol> symbols;
    private ListIterator<Symbol> iterator;
    private LinkedList<SymbolNode> symbolNodes;
    private Symbol currentSymbol;


    public GrammarParser(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public LinkedList<SymbolNode> parseCode() {
        symbolNodes = new LinkedList<>();
        iterator = this.symbols.listIterator();
        while (iterator.hasNext()) {
            SymbolNode node = parseStmt();
            if(!node.equals(null)) {
                symbolNodes.add(node);
            }
        }
        return symbolNodes;
    }

    private SymbolNode parseStmt() {
        switch(getNextSymbolType()){
            case SymbolType.IF:return parseIfStmt();
            case SymbolType.WHILE:return parseWhileStmt();
            case SymbolType.WRITE:return parseWriteStmt();
            case SymbolType.LBRACE:return parseStmtBlock();
            case SymbolType.IDENTIFIER:return parseAssignStmt();
            case SymbolType.INT:
            case SymbolType.DOUBLE:return parseDeclareStmt();
            default:
                return null;
        }
    }

    private SymbolNode parseIfStmt() {
        SymbolNode node = new SymbolNode(SymbolNodeType.IF_STMT);
        comfirmNextSymbol(SymbolType.IF);
        comfirmNextSymbol(SymbolType.LPAR);
        node.setChild(parseJExp());
        comfirmNextSymbol(SymbolType.RPAR);
        node.setChild(parseStmt());
        if(getNextSymbolType() == SymbolType.ELSE){
            comfirmNextSymbol(SymbolType.ELSE);
            node.setChild(parseStmt());
        }
        return node;
    }

    private SymbolNode parseWhileStmt(){
        SymbolNode node = new SymbolNode(SymbolNodeType.WHILE_STMT);
        comfirmNextSymbol(SymbolType.WHILE);
        comfirmNextSymbol(SymbolType.LPAR);
        node.setChild(parseJExp());
        comfirmNextSymbol(SymbolType.RPAR);
        node.setChild(parseStmt());
        return node;
    }

    private SymbolNode parseWriteStmt(){
        SymbolNode node = new SymbolNode(SymbolNodeType.WRITE_STMT);
        comfirmNextSymbol(SymbolType.WRITE);
        comfirmNextSymbol(SymbolType.LPAR);
        SymbolNode exp = parseExp();
        node.setChild(exp);
        comfirmNextSymbol(SymbolType.RPAR);
        comfirmNextSymbol(SymbolType.SEMI);
        return node;
    }

    private SymbolNode parseStmtBlock(){
        SymbolNode node = new SymbolNode(SymbolNodeType.NULL);
        SymbolNode head = node;
        SymbolNode temp;
        comfirmNextSymbol(SymbolType.LBRACE);
        while(getNextSymbolType()!=SymbolType.RBRACE){
            temp = parseStmt();
            try {
                node.setNextNode(temp);
            }catch (NullPointerException e){
                return head;
            }
            node = temp;
        }
        comfirmNextSymbol(SymbolType.RBRACE);
        return head;
    }

    private SymbolNode parseDeclareStmt(){
        SymbolNode node = new SymbolNode(SymbolNodeType.DECLARE_STMT);
        //声明语句的变量节点
        SymbolNode varNode;
        switch (getNextSymbolType()){
            case SymbolType.INT:
                comfirmNextSymbol(SymbolType.INT);
                varNode = new SymbolNode(SymbolNodeType.INT_VAR);
                break;
            case SymbolType.DOUBLE:
                comfirmNextSymbol(SymbolType.DOUBLE);
                varNode = new SymbolNode(SymbolNodeType.DOUBLE_VAR);
                break;
            default:
                return null;
        }
        comfirmNextSymbol(SymbolType.IDENTIFIER);
        varNode.setContent(currentSymbol.getSymbolName());
        node.setChild(varNode);
        if(getNextSymbolType() == SymbolType.ASSIGN){
            comfirmNextSymbol(SymbolType.ASSIGN);
            node.setChild(parseExp());
        }
        comfirmNextSymbol(SymbolType.SEMI);
        return node;
    }

    private SymbolNode parseAssignStmt(){
        SymbolNode node = new SymbolNode(SymbolNodeType.ASSIGN_STMT);
        SymbolNode varNode = new SymbolNode(SymbolNodeType.VAR);
        comfirmNextSymbol(SymbolType.IDENTIFIER);
        varNode.setContent(currentSymbol.getSymbolName());
        node.setChild(varNode);
        comfirmNextSymbol(SymbolType.ASSIGN);
        node.setChild(parseExp());
        comfirmNextSymbol(SymbolType.SEMI);
        return node;
    }

    private SymbolNode parseJExp(){
        SymbolNode node = new SymbolNode(SymbolNodeType.JEXP);
        SymbolNode Lexp = parseExp();
        int type = checkNextSymbolType(SymbolType.GREATER,SymbolType.GOE,SymbolType.LESS,SymbolType.LOE,
                SymbolType.EQUAL,SymbolType.NEQUAL);
        comfirmNextSymbol(type);
        node.setDataType(type);
        SymbolNode Rexp = parseExp();
        node.setChild(Lexp);
        node.setChild(Rexp);
        return node;
    }

    private SymbolNode parseExp(){
        SymbolNode node = new SymbolNode(SymbolNodeType.EXP);
        SymbolNode term = parseTerm();
        int type = checkNextSymbolType(SymbolType.PLUS,SymbolType.MINUS,SymbolType.MOD);
        if(type == -1){
            return term;
        }
        node.setDataType(type);
        comfirmNextSymbol(type);
        SymbolNode exp = parseExp();
        node.setChild(term);
        node.setChild(exp);
        return node;
    }

    private SymbolNode parseTerm(){
        SymbolNode node = new SymbolNode(SymbolNodeType.EXP);
        SymbolNode factor = parseFactor();
        int type = checkNextSymbolType(SymbolType.MULTIPLY,SymbolType.DIVIDE);
        node.setDataType(type);
        if(type == -1){
            return factor;
        }
        comfirmNextSymbol(type);
        SymbolNode term = parseTerm();
        node.setChild(factor);
        node.setChild(term);
        return node;
    }

    // factor -> ( exp ) | NUMBER | IDENTIFIER
    private SymbolNode parseFactor(){
        SymbolNode node = new SymbolNode(SymbolNodeType.FACTOR);
        switch (getNextSymbolType()){
            case SymbolType.LPAR:
                comfirmNextSymbol(SymbolType.LPAR);
                SymbolNode exp = parseExp();
                node.setChild(exp);
                comfirmNextSymbol(SymbolType.RPAR);
                return node;
            case SymbolType.IDENTIFIER:
                comfirmNextSymbol(SymbolType.IDENTIFIER);
                SymbolNode varNode = new SymbolNode(SymbolNodeType.VAR);
                varNode.setContent(currentSymbol.getSymbolName());
                node.setChild(varNode);
                return node;
            case SymbolType.INTNUM:
                comfirmNextSymbol(SymbolType.INTNUM);
                SymbolNode inum = new SymbolNode(SymbolNodeType.NUM);
                inum.setContent(currentSymbol.getSymbolName());
                node.setChild(inum);
                return node;
            case SymbolType.DOUBLENUM:
                comfirmNextSymbol(SymbolType.DOUBLENUM);
                SymbolNode dnum = new SymbolNode(SymbolNodeType.NUM);
                dnum.setContent(currentSymbol.getSymbolName());
                node.setChild(dnum);
                return node;
            default:
                String str = "line " + currentSymbol.getLine() + " : GrammarError";
                CodeError error5 = new CodeError(5, str);
                System.out.println(error5);
        }
        return node;
    }

    private void comfirmNextSymbol(int type) {
        try {
            currentSymbol = iterator.next();
        }catch (Exception e){
            String str = "line " + (currentSymbol.getLine()-1) + " : GrammarError";
            CodeError error5 = new CodeError(5, str);
            System.out.println(error5);
        }
        if (currentSymbol.getSymbolTypeId() == type) {
            return;
        }
        String str = "line " + (currentSymbol.getLine()-1) + " : GrammarError";
        CodeError error5 = new CodeError(5, str);
        System.out.println(error5);
    }

    private int getNextSymbolType() {
        if(iterator.hasNext()){
            int i = iterator.next().getSymbolTypeId();
            iterator.previous();
            return i;
        }
        return -1;
    }


    private int checkNextSymbolType(int ... types){
        for(int type:types){
            if(getNextSymbolType() == type)
                return type;
        }
        return -1;
    }
}

