package net.sumile.sumile.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/28.
 */
public class MainData implements Serializable {
    private String title;
    private String desc;
    private String date;

    private Class clz;

    public MainData(String title, String desc, String date, Class clz) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.clz = clz;
    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
