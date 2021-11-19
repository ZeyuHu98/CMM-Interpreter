package classes;

/**
 * Created by Hzy on 2018/11/27.
 * 树节点的类型
 */
public class SymbolNodeType {
    /**
     * 语句块使用链表存储,使用NULL类型的TreeNode作为头部节点,
     * 不使用NULL的节点存储信息,仅仅使用next指向下一个TreeNode
     */
    public static final int NULL = 0;
    /**
     * if语句
     * child1存放exp表达式
     * child2存放if条件正确时的TreeNode
     * child3存放else的TreeNode，如果有的话
     */
    public static final int IF_STMT = 1;
    /**
     * child1存储EXP
     * child2存储循环体第一个语句
     */
    public static final int WHILE_STMT = 2;
    /**
     * 声明语句
     * child1中存放VAR节点
     * 如果有赋值EXP,则存放child2中
     */
    public static final int DECLARE_STMT = 3;
    /**
     * 赋值语句
     * child1存放var节点
     * child2存放exp节点
     */
    public static final int ASSIGN_STMT = 4;
    /**
     * 输出语句
     * child1存放表达式
     */
    public static final int WRITE_STMT = 5;
    /**
     * 逻辑表达式，形如exp logicOP exp
     * datatype保存逻辑运算符种别码
     * child1保存左exp，child2保存右exp
     * value==null
     */
    public static final int JEXP = 6;
    /**
     * 表达式
     * dataype存放运算符种别码
     * name存放变量名
     */
    public static final int EXP = 7;
    /**
     * 因子
     * 有符号datatype存储TOKEN.PLUS/MINUS,默认为Token.PLUS
     * 只会在left中存放一个TreeNode
     */
    public static final int FACTOR = 8;
    /**
     * 运算符
     * 在datatype中存储操作符类型
     */
    public static final int INT_VAR = 9;

    public static final int DOUBLE_VAR = 10;

    public static final int VAR = 11;

    public static final int NUM = 12;

}
