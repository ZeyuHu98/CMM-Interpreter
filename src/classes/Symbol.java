package classes;

/**
 * Created by Hzy on 2018/10/30.
 * symbol类（词法分析）
 */
public class Symbol {
    //存储种别码
    private int symbolTypeId;
    //存储符号类型
    private String symbolType;
    //标识符名字
    private String symbolName;
    //symbol所在行号
    private int line;
    //symbol在其所在行的第几个
    private int lineNum;

    public Symbol(int symbolTypeId, String symbolType, String symbolName, int line, int lineNum) {
        this.symbolTypeId = symbolTypeId;
        this.symbolType = symbolType;
        this.symbolName = symbolName;
        this.line = line;
        this.lineNum = lineNum;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public int getLine() {
        return line;
    }

    public int getSymbolTypeId() {
        return symbolTypeId;
    }

    @Override
    public String toString(){
        String str = "第"+line+"行"+"第"+lineNum +"个符号: "+ symbolType + "--" + symbolName;
        return str;
    }
}
