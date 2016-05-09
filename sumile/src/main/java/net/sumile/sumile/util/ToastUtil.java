package net.sumile.sumile.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ToastUtil {
    public static void showShortToast(Context mContext, String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }
}
