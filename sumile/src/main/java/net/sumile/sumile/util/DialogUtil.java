package net.sumile.sumile.util;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.tv.TvContentRating;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ReplacementTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sumile.sumile.R;

/**
 * Created by Administrator on 2016/5/8.
 */
public class DialogUtil {
    public static void showTextDialog(final Context context, String content) {
        AlertDialog.Builder builder = getBuilderInstance(context);
        mView = LayoutInflater.from(context).inflate(R.layout.textview, null);
        tv = (TextView) mView.findViewById(R.id.textView);
        tv.setText(content);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDialogShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, tv.getText().toString().trim()));
                ToastUtil.showShortToast(context, "已经复制到剪贴板");
                return true;
            }
        });
        builder.setView(mView);
        mDialog = builder.create();
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public static AlertDialog mDialog;

    private DialogUtil() {

    }

    public static boolean isDialogShowing() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private static AlertDialog.Builder mBuilder;
    private static View mView;
    private static TextView tv;

    public static AlertDialog.Builder getBuilderInstance(Context context) {
        if (mBuilder == null) {
            synchronized (DialogUtil.class) {
                if (mBuilder == null) {
                    mBuilder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                }
            }
        }
        return mBuilder;
    }
}
