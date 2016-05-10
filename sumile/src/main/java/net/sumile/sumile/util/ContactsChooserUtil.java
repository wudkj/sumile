package net.sumile.sumile.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import net.sumile.sumile.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

/**
 * Created by Administrator on 2016/5/8.
 * 需要layout文件dialog_layout以及item_dialog
 */
public class ContactsChooserUtil {
    private final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    /**
     * 电话号码
     **/
    private final int PHONES_NUMBER_INDEX = 1;
    /**
     * 联系人显示名称
     **/
    private final int PHONES_DISPLAY_NAME_INDEX = 0;
    private ArrayList<NameNumberPair> mContacts = new ArrayList<NameNumberPair>();
    private ArrayList<NameNumberPair> mSelected = new ArrayList<NameNumberPair>();
    private Activity mActivity;
    private ContactsChooserImpl impl;

    public ContactsChooserUtil(Activity activity, ContactsChooserImpl impl) {
        this.impl = impl;
        getContacts(activity);
    }

    public void getContacts(Activity activity) {
        mActivity = activity;
        ContentResolver resolver = mActivity.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            mContacts.clear();
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);
                NameNumberPair pair = new NameNumberPair();
                pair.setName(contactName);
                pair.setPhoneNumber(phoneNumber);
                mContacts.add(pair);
            }
            phoneCursor.close();
        }
        showSelectListView();
    }

    final HashMap<Integer, Boolean> sign = new HashMap<>();
    int checkedTotal = 0;
    private int checkedCount = 0;

    private void showSelectListView() {
        Collections.sort(mContacts, new SortUtil());
        initShow();
        setUpAdapter();
        createDialog(mActivity, mAdapter);
        initAction();
        show();
    }

    private void initShow() {
        checkedTotal = 0;
        if (sign.size() == 0) {
            for (int i = 0; i < mContacts.size(); i++) {
                sign.put(i, false);
            }
        } else {
            for (Map.Entry<Integer, Boolean> item : sign.entrySet()) {
                if (!item.getValue()) {
                    if (allChecked == null) {
                        allChecked.setChecked(false);
                        break;
                    } else {
                        allChecked.setChecked(true);
                    }
                }
            }
        }
    }

    private void initAction() {
        fanxuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Map.Entry<Integer, Boolean> item : sign.entrySet()) {
                    if (item.getValue()) {
                        item.setValue(false);
                    } else {
                        item.setValue(true);
                    }
                }
                mAdapter.notifyDataSetChanged();
                invalideAllButton();
                invalideTotalCount();
            }
        });
        allChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allChecked.isChecked()) {
                    for (Map.Entry<Integer, Boolean> item : sign.entrySet()) {
                        item.setValue(true);
                    }
                    checkedTotal = sign.size();
                } else {
                    for (Map.Entry<Integer, Boolean> item : sign.entrySet()) {
                        item.setValue(false);
                    }
                    checkedTotal = 0;
                }
                tv_total.setText("已选择" + checkedTotal + "个联系人");
                mAdapter.notifyDataSetChanged();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelected.clear();
                for (Map.Entry<Integer, Boolean> item : sign.entrySet()) {
                    if (item.getValue()) {
                        NameNumberPair pair = new NameNumberPair();
                        pair.setName(mContacts.get(item.getKey()).getName());
                        pair.setPhoneNumber(mContacts.get(item.getKey()).getPhoneNumber());
                        mSelected.add(pair);
//                        Toast.makeText(SendMessageActivity.this, mContactsNames.get(item.getKey()) + mContactsNumbers.get(item.getKey()), Toast.LENGTH_SHORT).show();
                    }
                }
                Collections.sort(mSelected, new SortUtil());
                impl.onContactsChoosed(mSelected);
                dismissDialog();
            }
        });
    }

    private void dismissDialog() {
        if (mActivity != null && !mActivity.isFinishing())
            if (isShowing()) {
                if (mDialog == null) {
                    return;
                }
                mDialog.dismiss();
            }
    }

    public interface ContactsChooserImpl {
        void onContactsChoosed(ArrayList<NameNumberPair> selectedNameNumbers);
    }

    private void setUpAdapter() {
        mAdapter = new RecyclerView.Adapter<MyViewHolder>() {
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_dialog, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(final MyViewHolder holder, final int position) {
                holder.mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.checkBox.performClick();
                    }
                });
                holder.tv_name.setText(mContacts.get(position).getName());
                holder.telephone.setText(mContacts.get(position).getPhoneNumber());
//                holder.checkBox.setChecked(false);
                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sign.put(position, holder.checkBox.isChecked());
                        invalideTotalCount();
                        invalideAllButton();
                    }
                });
                holder.checkBox.setChecked(sign.get(position));
            }


            @Override
            public int getItemCount() {
                return mContacts.size();
            }
        };
    }

    private void invalideTotalCount() {
        checkedTotal = 0;
        for (Map.Entry<Integer, Boolean> item : sign.entrySet()) {
            if (item.getValue()) {
                checkedTotal++;
            }
        }
        tv_total.setText("已选择" + checkedTotal + "个联系人");
    }

    private void invalideAllButton() {
        checkedCount = 0;
        for (Map.Entry<Integer, Boolean> item : sign.entrySet()) {
            if (!item.getValue()) {
                //有没有勾选的
                break;
            } else {
                checkedCount++;
            }
        }
        if (checkedCount == sign.size()) {
            if (!allChecked.isChecked()) {
                allChecked.setChecked(true);
            }
        } else {
            if (allChecked.isChecked()) {
                allChecked.setChecked(false);
            }
        }
    }

    private RecyclerView.Adapter<MyViewHolder> mAdapter;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private CheckBox checkBox;
        private TextView telephone;
        private View mRootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.id_tv_name);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            telephone = (TextView) itemView.findViewById(R.id.tele_number);
            mRootView = itemView.findViewById(R.id.id_chooserRootView);
        }
    }

    public class NameNumberPair {
        private String name;
        private String phoneNumber;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    //========================================
    private Dialog mDialog;
    public TextView tv_total;
    public CheckBox allChecked;
    public TextView fanxuan;
    public TextView positiveButton;
    public TextView negativeButton;

    public void createDialog(Context context, RecyclerView.Adapter adapter) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        allChecked = (CheckBox) view.findViewById(R.id.allChecked);
        positiveButton = (TextView) view.findViewById(R.id.positiveButton);
        negativeButton = (TextView) view.findViewById(R.id.negativeButton);
        fanxuan = (TextView) view.findViewById(R.id.fanxuan);
        tv_total = (TextView) view.findViewById(R.id.textView);
        listView = (RecyclerView) view.findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setAdapter(adapter);
        mDialog.setContentView(view);
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
            if (!(mActivity).isFinishing()) {
                try {
                    mDialog.show();
                } catch (Exception e) {

                }
            }
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
