package controller;

import classes.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Pattern;

import static classes.Util.*;


/**
 * Created by Hzy on 2018/11/26.
 * 语义分析程序
 */
public class MeaningParser {
    //保存语法分析的结果
    private LinkedList<SymbolNode> symbolNodes;
    //迭代器
    private ListIterator<SymbolNode> iterator;
    //保存当前分析的节点
    private SymbolNode currentNode;
    //符号表
    private Set<Element> elementTable = new HashSet<>();
    //用于区分据变量的层次，初始值为0；
    private int space = 0;

    private StringBuilder output = new StringBuilder();

    private CodeError error6 = new CodeError(6, "Identifier already exists!");

    private CodeError error7 = new CodeError(7, "Undefined variable used!");

    private CodeError error8 = new CodeError(8, "Wrong type variable used!");

    public void getOutput(){
        System.out.println(this.output.toString());
    }

    public MeaningParser(LinkedList<SymbolNode> symbolNodes) {
        this.symbolNodes = symbolNodes;
        iterator = this.symbolNodes.listIterator();

    }

    public void parseMeaning(){
        while (iterator.hasNext()) {
            parseNode();
        }
    }

    private void parseNode(){
        int type = getNextNodeType();
        if(type != -1){
            currentNode = iterator.next();
        }else {
            return;
        }
        switch (type){
            case SymbolNodeType.IF_STMT: parseIfStmt();return;
            case SymbolNodeType.WHILE_STMT: parseWhileStmt();return;
            case SymbolNodeType.DECLARE_STMT: parseDeclareStmt();return;
            case SymbolNodeType.ASSIGN_STMT: parseAssignStmt();return;
            case SymbolNodeType.WRITE_STMT: parseWriteStmt();return;
        }
    }

    private void parseIfStmt(){
        if(parseJExp(currentNode.getChild().get(0))){
            parseStmtBlock(currentNode.getChild().get(1));
        }else if(currentNode.getChild().size()==3){
            parseStmtBlock(currentNode.getChild().get(2));
        }
    }

    private void parseWhileStmt(){
        SymbolNode loopNode = currentNode;
        SymbolNode jexp = currentNode.getChild().get(0);
        while(parseJExp(jexp)){
            parseStmtBlock(loopNode.getChild().get(1));
        }
    }

    private void parseWriteStmt(){
        if(currentNode.getChild().get(0).getNodeType() == SymbolNodeType.EXP){
            output.append(parseExp(currentNode.getChild().get(0))+"\r\n");
        }else{
            output.append(parseFactor(currentNode.getChild().get(0))+"\r\n");
        }
    }

    private void parseDeclareStmt(){
        SymbolNode varNode = currentNode.getChild().get(0);
        for(Element element:elementTable){
            if (varNode.getContent().equals(element.getName())){
                System.out.println(error6);
                break;
            }
        }
        String name = varNode.getContent();
        boolean type = varNode.getNodeType() == SymbolNodeType.INT_VAR;
        String value;
        if(currentNode.getNodeType() == SymbolNodeType.EXP){
            value = parseExp(currentNode.getChild().get(1));
        }else{
            value = parseFactor(currentNode.getChild().get(1));
        }
        if(type == typeJudge(value)){
            Element element = new Element(name,type,value,this.space);
            elementTable.add(element);
        }else{
            System.out.println(error8);
        }
    }

    private void parseAssignStmt(){
        SymbolNode varNode = currentNode.getChild().get(0);
        for(Element element:elementTable) {
            String str;
            if (varNode.getContent().equals(element.getName())) {
                if(currentNode.getChild().get(1).getNodeType() == SymbolNodeType.EXP) {
                    str = parseExp(currentNode.getChild().get(1));
                }else {
                    str = parseFactor(currentNode.getChild().get(1));
                }
                if((typeJudge(str)&&element.isType())||(!typeJudge(str)&&!element.isType())){
                    element.setValue(str);
                }
                else {
                    System.out.println(error8);
                }
                return;
            }
        }
        System.out.println(error7);
    }

    private void parseStmtBlock(SymbolNode head){
        this.space++;
        SymbolNode temp = head;
        while(temp.getNextNode() != null){
            this.currentNode = temp.getNextNode();
            parseStmtCode();
            temp = temp.getNextNode();
        }

        for(Element element:elementTable){
            if(element.getSpace() == this.space){
                elementTable.remove(element);
            }
        }
        this.space--;
    }

    private void parseStmtCode(){
        int type = currentNode.getNodeType();
        switch (type){
            case SymbolNodeType.IF_STMT: parseIfStmt();return;
            case SymbolNodeType.WHILE_STMT: parseWhileStmt();return;
            case SymbolNodeType.DECLARE_STMT: parseDeclareStmt();return;
            case SymbolNodeType.ASSIGN_STMT: parseAssignStmt();return;
            case SymbolNodeType.WRITE_STMT: parseWriteStmt();return;
        }
    }

    private boolean parseJExp(SymbolNode node){
        String ls;
        String rs;
        if(node.getChild().get(0).getNodeType() == SymbolNodeType.EXP){
            ls = parseExp(node.getChild().get(0));
        }else {
            ls = parseFactor(node.getChild().get(0));
        }
        if(node.getChild().get(1).getNodeType() == SymbolNodeType.EXP){
            rs = parseExp(node.getChild().get(1));
        }else {
            rs = parseFactor(node.getChild().get(1));
        }
        if(typeJudge(ls)){
            if(typeJudge(rs)){
                return jexpCalculate(Integer.parseInt(ls),Integer.parseInt(rs),node.getDataType());
            }else{
                return jexpCalculate(Integer.parseInt(ls),Double.parseDouble(rs),node.getDataType());
            }
        }else{
            if(typeJudge(rs)){
                return jexpCalculate(Double.parseDouble(ls),Integer.parseInt(rs),node.getDataType());
            }else{
                return jexpCalculate(Double.parseDouble(ls),Double.parseDouble(rs),node.getDataType());
            }
        }
    }

    private String parseExp(SymbolNode node){
        String ls, rs;
        if(node.getChild().get(0).getNodeType() == SymbolNodeType.EXP){
            ls = parseExp(node.getChild().get(0));
        }else {
            ls = parseFactor(node.getChild().get(0));
        }
        if(node.getChild().get(1).getNodeType() == SymbolNodeType.EXP){
            rs = parseExp(node.getChild().get(1));
        }else {
            rs = parseFactor(node.getChild().get(1));
        }
        if(typeJudge(ls)){
            if(typeJudge(rs)){
                return String.valueOf(expCalculate(Integer.parseInt(ls),Integer.parseInt(rs),node.getDataType()));
            }else{
                return String.valueOf(expCalculate(Integer.parseInt(ls),Double.parseDouble(rs),node.getDataType()));
            }
        }else {
            if (typeJudge(rs)) {
                return String.valueOf(expCalculate(Double.parseDouble(ls),Integer.parseInt(rs),node.getDataType()));
            } else {
                return String.valueOf(expCalculate(Double.parseDouble(ls),Double.parseDouble(rs),node.getDataType()));
            }
        }
    }

    private String parseFactor(SymbolNode node){
        int type = node.getChild().get(0).getNodeType();
        switch (type){
            case SymbolNodeType.EXP:
                return parseExp(node.getChild().get(0));
            case SymbolNodeType.VAR:
            case SymbolNodeType.INT_VAR:
            case SymbolNodeType.DOUBLE_VAR:
                for(Element element:elementTable){
                    if(element.getName().equals(node.getChild().get(0).getContent())){
                        return element.getValue();
                    }
                }
                System.out.println(error7);
            case SymbolNodeType.NUM:
                return node.getChild().get(0).getContent();
            default:
                return null;
        }
    }

    private int getNextNodeType(){
        if(iterator.hasNext()){
            int type = iterator.next().getNodeType();
            iterator.previous();
            return type;
        }
        return -1;
    }
    //判断为int还是double类型值的方法，int返回true，double返回false
    private boolean typeJudge(String str){
        return !Pattern.matches("^\\d+\\.\\d+",str);
    }
}
