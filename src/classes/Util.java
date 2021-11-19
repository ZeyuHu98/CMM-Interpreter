package classes;

/**
 * Created by Hzy on 2019/1/1.
 */
public class Util {
    public static int expCalculate(int a, int b, int dataType){
        switch(dataType){
            case SymbolType.PLUS:return a+b;
            case SymbolType.MINUS:return a-b;
            case SymbolType.MULTIPLY:return a*b;
            case SymbolType.DIVIDE:return a/b;
            case SymbolType.MOD:return a%b;
            default:return 0;
        }
    }

    public static double expCalculate(int a, double b, int dataType){
        switch(dataType){
            case SymbolType.PLUS:return a+b;
            case SymbolType.MINUS:return a-b;
            case SymbolType.MULTIPLY:return a*b;
            case SymbolType.DIVIDE:return a/b;
            default:return 0;
        }
    }

    public static double expCalculate(double a, int b, int dataType){
        switch(dataType){
            case SymbolType.PLUS:return a+b;
            case SymbolType.MINUS:return a-b;
            case SymbolType.MULTIPLY:return a*b;
            case SymbolType.DIVIDE:return a/b;
            default:return 0;
        }
    }

    public static double expCalculate(double a, double b, int dataType){
        switch(dataType){
            case SymbolType.PLUS:return a+b;
            case SymbolType.MINUS:return a-b;
            case SymbolType.MULTIPLY:return a*b;
            case SymbolType.DIVIDE:return a/b;
            default:return 0;
        }
    }

    public static boolean jexpCalculate(int a, int b, int dataType){
        switch(dataType){
            case SymbolType.GREATER:return a>b;
            case SymbolType.GOE:return a>=b;
            case SymbolType.LESS:return a<b;
            case SymbolType.LOE:return a<=b;
            case SymbolType.EQUAL:return a==b;
            case SymbolType.NEQUAL:return a!=b;
            default:return true;
        }
    }

    public static boolean jexpCalculate(int a, double b, int dataType){
        switch(dataType){
            case SymbolType.GREATER:return a>b;
            case SymbolType.GOE:return a>=b;
            case SymbolType.LESS:return a<b;
            case SymbolType.LOE:return a<=b;
            case SymbolType.EQUAL:return a==b;
            case SymbolType.NEQUAL:return a!=b;
            default:return true;
        }
    }

    public static boolean jexpCalculate(double a, int b, int dataType){
        switch(dataType){
            case SymbolType.GREATER:return a>b;
            case SymbolType.GOE:return a>=b;
            case SymbolType.LESS:return a<b;
            case SymbolType.LOE:return a<=b;
            case SymbolType.EQUAL:return a==b;
            case SymbolType.NEQUAL:return a!=b;
            default:return true;
        }
    }

    public static boolean jexpCalculate(double a, double b, int dataType){
        switch(dataType){
            case SymbolType.GREATER:return a>b;
            case SymbolType.GOE:return a>=b;
            case SymbolType.LESS:return a<b;
            case SymbolType.LOE:return a<=b;
            case SymbolType.EQUAL:return a==b;
            case SymbolType.NEQUAL:return a!=b;
            default:return true;
        }
    }
}
