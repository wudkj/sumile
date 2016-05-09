package net.sumile.message.data;

/**
 * Created by zhangxiaokang on 16/4/12.
 */
public class Festival {
    private int id;
    private String name;
    public Festival(int id,String name){
        this.id=id;
        this.name=name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
