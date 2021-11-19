package classes;

/**
 * Created by Hzy on 2018/12/29.
 * 符号表元素类，用于表示符号表的子项
 */

public class Element {
    private String name;
    //用来保存数值种类，int为true，double为false
    private boolean type;
    private String value;
    private int space;

    public Element(String name, boolean type, String value, int space) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.space = space;
    }

    public int getSpace() {
        return space;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
