package net.sumile.sumile.data;

import net.sumile.sumile.activity.ContactChooserActivity;
import net.sumile.sumile.bean.MainData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/28.
 */
public class MainDataProvider {
    public static ArrayList<MainData> getMainDatas() {
        ArrayList<MainData> mData = new ArrayList<>();
        mData.add(getMainData("获取联系人信息", "多种获取联系人信息的方法，有的可以获取手机卡的，有的不能获取手机卡的，还有一种是我写的，可以多选获取选中联系人的", "2016年5月8日", ContactChooserActivity.class));
        return mData;
    }

    private static MainData getMainData(String title, String desc, String date, Class clz) {
        return new MainData(title, desc, date, clz);
    }
}
