package net.sumile.sumile.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by Administrator on 2016/5/9.
 */
public class CommonUtil {
    public static void copyToClipBoard(Context context, String content) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, content));
        ToastUtil.showShortToast(context, "已经复制到剪贴板");
    }
}
