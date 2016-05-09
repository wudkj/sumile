package net.sumile.sumile.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Administrator on 2016/4/28.
 */
public class IntentHelper {
    public static void openInWebView(Context mContext, String url) {
        if (!url.contains("http") && !url.contains("https")) {
            url = "http://" + url;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        mContext.startActivity(intent);
    }
}
