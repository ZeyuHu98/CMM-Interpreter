package classes;

import java.util.LinkedList;

/**
 * Created by Hzy on 2018/11/27.
 * 树节点实体类
 */
public class SymbolNode {
    //节点类型
    private int nodeType;
    //子节点集合
    private LinkedList<SymbolNode> childs;
    //下一条代码（顶级代码存储在集合中，不使用这条参数）
    private SymbolNode nextNode = null;
    //需要时保存种别码
    private int dataType;
    //保存变量名
    private String content;
    
    //构造函数
    public SymbolNode(int nodeType){
        this.nodeType = nodeType;
        childs = new LinkedList<>();
    }
    //添加孩子节点


    public int getNodeType() {
        return nodeType;
    }

    public LinkedList<SymbolNode> getChild() {
        return childs;
    }

    public void setChild(SymbolNode node) {
        this.childs.add(node);
    }
    public SymbolNode getNextNode() {
        return nextNode;
    }

    public String getContent() {
        return content;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
    //设置下一节点。该属性仅仅适用于TreeNodeType.NULL的头部节点
    public void setNextNode(SymbolNode node){
        this.nextNode = node;
    }
    //设置变量名
    public void setContent(String content) {
        this.content = content;
    }
    //重写toString方法用于打印输出
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        switch (this.nodeType){
            case SymbolNodeType.NULL:
                SymbolNode node = this;
                str.append("{\r\n");
                while(!(node.nextNode == null)){
                    str.append(node.nextNode.toString()+"\r\n");
                    node = node.nextNode;
                }
                str.append("}");
                break;
            case SymbolNodeType.IF_STMT:
                str.append("if语句，判断表达式：\r\n\t"+this.childs.get(0).toString()+"\r\n语句：\r\n"+this.childs.get(1).toString()+"\r\n");
                if(this.childs.size()>2){
                    str.append("else语句：\r\n\t"+this.childs.get(2).toString());
                }
                break;
            case SymbolNodeType.WHILE_STMT:
                str.append("while语句，判断表达式：\r\n\t"+this.childs.get(0).toString()+"\r\n语句：\r\n"+ this.childs.get(1).toString()+"\r\n");
                break;
            case SymbolNodeType.WRITE_STMT:
                str.append("write语句，输出："+this.childs.get(0).toString());
                break;
            case SymbolNodeType.DECLARE_STMT:
                str.append("声明语句：");
                if(this.childs.get(0).nodeType == SymbolNodeType.INT_VAR){
                    str.append("int变量");
                }else{
                    str.append("double变量");
                }
                str.append(this.childs.get(0).content+"="+this.childs.get(1).toString());
                break;
            case SymbolNodeType.ASSIGN_STMT:
                str.append("赋值语句："+this.childs.get(0).content+"="+this.childs.get(1).toString());
                break;
            case SymbolNodeType.JEXP:
            case SymbolNodeType.EXP:
            case SymbolNodeType.FACTOR:
                str.append("\t"+this.childs.get(0).toString());
                if(this.childs.size() == 2){
                    str.append("，符号："+this.dataType+"，"+this.childs.get(1).toString());
                }
                break;
            case SymbolNodeType.INT_VAR:
            case SymbolNodeType.DOUBLE_VAR:
            case SymbolNodeType.VAR:
                str.append(this.content);
                break;
            case SymbolNodeType.NUM:
                str.append(this.content);
                break;
        }
        return str.toString();
    }

}
