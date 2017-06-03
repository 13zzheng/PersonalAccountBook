package stu.edu.cn.zing.personalbook.model;

/**
 * Created by Administrator on 2017/4/30.
 */

public class QueryCriteria {

    public static final int VALUE_TYPE_INT = 1;
    public static final int VALUE_TYPE_STRING = 2;

    private String name;
    private String value;
    private int valueType;

    public int getValueType() {
        return valueType;
    }

    public void setValueType(int valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
