package net.sumile.sumile.bean;

/**
 * Created by Administrator on 2016/5/8.
 */
public class DescribeAndCode {
    private String describe;
    private String code;

    public DescribeAndCode(String describe, String code) {
        this.describe = describe;
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
