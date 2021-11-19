package classes;

/**
 * Created by Hzy on 2018/10/30.
 * Error类用于提示错误信息
 */
public class CodeError {
    private String errorName;
    private int errorId;
    public CodeError(int errorId, String errorName) {
        this.errorId = errorId;
        this.errorName = errorName;
    }
    @Override
    public String toString(){
        return "error: ["+"type:"+errorId+" name:"+errorName+"]";
    }
}
