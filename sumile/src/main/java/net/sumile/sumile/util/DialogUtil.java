package net.sumile.sumile.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.tv.TvContentRating;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ReplacementTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sumile.sumile.R;

/**
 * Created by Administrator on 2016/5/8.
 */
public class DialogUtil {
    public static Dialog showTextDialog(final Context context, String content) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        AlertDialog.Builder builder = getBuilderInstance(context);
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
                CommonUtil.copyToClipBoard(context, tv.getText().toString().trim());
                return true;
            }
        });
        mDialog.setContentView(mView);
//        if (mDialog != null) {
//            if (!((Activity) context).isFinishing()) {
//                try {
//                    mDialog.show();
//                } catch (Exception e) {
//                    System.out.println(e.toString());
//                }
//            }
//        }
        mDialog.show();
        return mDialog;
    }

    public static Dialog mDialog;

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

    private static View mView;
    private static TextView tv;

}
