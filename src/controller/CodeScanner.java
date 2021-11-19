package controller;

import classes.CodeError;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hzy on 2018/10/30.
 * 代码读取程序
 */
//读取代码
public class CodeScanner {

    //定义error变量
    private static CodeError error1 = new CodeError(1,"FileNotFound");
    private static CodeError error2 = new CodeError(2,"FileError");
    private List<char[]> codes = new ArrayList<>();

    public List<char[]> scanCode(String filePath){
        File file = new File(filePath);
        try {
            if(file.isFile()&&file.exists()) {
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String lineCode = "";
                while( (lineCode = bufferedReader.readLine()) != null){
                    char[] code = new char[lineCode.length()+1];
                    for(int i = 0;i<lineCode.length();i++){
                        code[i] = lineCode.charAt(i);
                    }
                    code[lineCode.length()] = '\n';
                    codes.add(code);
                }
                bufferedReader.close();
            }
            else {
                System.out.println(error1.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(error2.toString());
        }
        return this.codes;
    }
}
