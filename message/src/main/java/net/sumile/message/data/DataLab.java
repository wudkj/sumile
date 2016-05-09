package net.sumile.message.data;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaokang on 16/4/12.
 */
public class DataLab {
    private ArrayList<Festival> mData=new ArrayList<Festival>();
    private DataLab(){
        mData.add(new Festival(1,"国庆节"));
        mData.add(new Festival(0,"五一劳动节"));
        msgs.add(new Msg(0,0,"5011"));
        msgs.add(new Msg(1,1,"5011"));
        msgs.add(new Msg(2,1,"5011"));
        msgs.add(new Msg(3,1,"5011"));
        msgs.add(new Msg(4,1,"5011"));
        msgs.add(new Msg(5,1,"5011"));
    };
    private static  DataLab mInstance;
    public static DataLab getInstance(){
        if (mInstance==null){
            synchronized (DataLab.class){
                if (mInstance==null){
                    mInstance=new DataLab();
                }
            }
        }
        return mInstance;
    }
    public ArrayList<Festival> getData(){
        return new ArrayList<Festival>(mData);
    }
    private ArrayList<Msg> msgs=new ArrayList<Msg>();
    public ArrayList<Msg> getMSGList(int id){
        ArrayList<Msg> msgs2=new ArrayList<Msg>();
        for (Msg mgs:msgs){
            if (mgs.getFestivalId()==id){
                msgs2.add(mgs);
            }
        }
        return msgs2;
    }
    public Msg getMsgByMessageId(int id){
        for (Msg msg: msgs){
            if (msg.getFestivalId()==id){
                return msg;
            }
        }
        return null;
    }
}
