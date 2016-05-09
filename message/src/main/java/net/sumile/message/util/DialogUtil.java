package net.sumile.message.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import net.sumile.message.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangxiaokang on 16/4/15.
 */
public class DialogUtil {
    private AlertDialog mDialog;
    private AlertDialog.Builder builder;
    public TextView tv_total;
    public CheckBox allChecked;
    public TextView fanxuan;

    public AlertDialog.Builder getDialogBuilder() {
        return builder;
    }

    public DialogUtil createDialog(Context context, RecyclerView.Adapter adapter) {
        builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        allChecked = (CheckBox) view.findViewById(R.id.allChecked);
        fanxuan = (TextView) view.findViewById(R.id.fanxuan);
        tv_total = (TextView) view.findViewById(R.id.textView);
        listView = (RecyclerView) view.findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setAdapter(adapter);
        builder.setView(view);
        return this;
    }

    public boolean isShowing() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                return true;
            }
        }
        return false;
    }

    public void show() {
        if (isShowing()) {
            return;
        } else {
            mDialog = builder.create();
            mDialog.show();
        }
    }
//    private void initViewAdapter(final Context context, HashMap isChecked_hashMap) {
//        mAdapter = new RecyclerView.Adapter<MyViewHolder>() {
//
//            @Override
//            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dialog, parent, false));
//                return holder;
//            }
//
//            @Override
//            public void onBindViewHolder(MyViewHolder holder, int position) {
//                holder.tv_name.setText();
//            }
//
//            @Override
//            public int getItemCount() {
//                return 0;
//            }
//        };
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        private TextView tv_name;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            tv_name = (TextView) itemView.findViewById(R.id.id_tv_name);
//        }
//    }

    private RecyclerView listView;

    private void initViewAction(View v, RecyclerView.Adapter mAdapter) {

    }
}
