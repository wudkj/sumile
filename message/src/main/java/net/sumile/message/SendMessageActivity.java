package net.sumile.message;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.sumile.message.biz.SmsBiz;
import net.sumile.message.data.DB_MSG;
import net.sumile.message.data.DataLab;
import net.sumile.message.data.Festival;
import net.sumile.message.data.Msg;
import net.sumile.message.util.DialogUtil;
import net.sumile.message.view.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class SendMessageActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_GETCONTACTS = 1;
    private FrameLayout id_loading_layout;
    private Button addPeople;
    private ProgressBar sending_progress;
    private EditText id_et_content;
    private FloatingActionButton id_fab_send;
    private FlowLayout mFlowLayout;
    private LayoutInflater mInflate;
    private RecyclerView.Adapter<MyViewHolder> mAdapter;
    private Festival mFestival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smsUtil = new SmsBiz(this);
        setContentView(R.layout.activity_send_message);
        mInflate = LayoutInflater.from(this);
        initViews();
        initData();
        initActions();
        initReceiver();
    }

    private static final String ACTION_SEND_MSG = "ACTION_SEND_MSG";
    private PendingIntent mSendPI;
    private BroadcastReceiver mSentReceiver;
    private static final String ACTION_DELIVER_MSG = "ACTION_DELIVER_MSG";
    private PendingIntent mDeliverPI;
    private BroadcastReceiver mDeliverReceiver;
    private static String TAG = "sumile";
    private SmsBiz smsUtil;
    private int mMsgSendCount = 0;
    private int mMsgTotalCount = 0;

    private void initReceiver() {
        Intent intent = new Intent(ACTION_SEND_MSG);
        mSendPI = PendingIntent.getBroadcast(this, 0, intent, 0);
        registerReceiver(mSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mMsgSendCount++;
                if (getResultCode() == RESULT_OK) {
                    Log.i(TAG, "onReceive: 短信发送成功");
                    Toast.makeText(SendMessageActivity.this, "短信发送成功 (" + mMsgSendCount + "/" + mMsgTotalCount + ")", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "onReceive: 短信发送失败");
                }
                if (mMsgSendCount == mMsgTotalCount) {
                    finish();
                }
            }
        }, new IntentFilter(ACTION_SEND_MSG));
        Intent intentSend = new Intent(ACTION_SEND_MSG);
        mDeliverPI = PendingIntent.getBroadcast(this, 0, intentSend, 0);
        registerReceiver(mDeliverReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive: 联系人已经接受到短信了");
            }
        }, new IntentFilter(ACTION_DELIVER_MSG));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDeliverReceiver);
        unregisterReceiver(mSentReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GETCONTACTS) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                parseCursor(cursor);
            }
        }
    }

    private ArrayList<String> mContactsNames = new ArrayList<String>();
    private ArrayList<String> mContactsNumbers = new ArrayList<String>();
    private ArrayList<String> mContactsNamesSelected = new ArrayList<>();
    private ArrayList<String> mContactsNumbersSelected = new ArrayList<>();

    private void parseCursor(Cursor cursor) {
        cursor.moveToFirst();
        String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        mContactsNames.add(contactName);
        String number = getContactNumber(cursor);
        if (!TextUtils.isEmpty(number)) {
            mContactsNames.add(contactName);
            mContactsNumbers.add(number);
            addTag(contactName);
        }
    }

    private void removeAllTags() {
        mFlowLayout.removeAllViews();
    }

    private void addTag(String contactName) {
        TextView tv = (TextView) mInflate.inflate(R.layout.bg, mFlowLayout, false);
        tv.setText(contactName);
        mFlowLayout.addView(tv);
    }

    private String getContactNumber(Cursor cursor) {
        String phoneNumber = null;
        int numberCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        if (numberCount > 0) {
            int contactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phoneNumberCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactID, null, null);
            phoneNumberCursor.moveToFirst();
            phoneNumber = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumberCursor.close();
            cursor.close();
            return phoneNumber;
        }
        return phoneNumber;
    }

    private void initActions() {
        addPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getContactData();
                getSIMContacts();
            }
        });
        id_fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContactsNamesSelected.size() == 0) {
                    Toast.makeText(SendMessageActivity.this, "请先选择联系人", Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = id_et_content.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(SendMessageActivity.this, "短信内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                id_loading_layout.setVisibility(View.VISIBLE);
                mMsgTotalCount = smsUtil.sendMsg(mContactsNumbersSelected, buildSendMsg(msg), mSendPI, mDeliverPI);
                mMsgSendCount = 0;
            }
        });
    }

    private DB_MSG buildSendMsg(String msg) {
        DB_MSG sendMes = new DB_MSG();
        sendMes.setMsg(msg);
        sendMes.setFestivalName(mFestival.getName());
        StringBuilder names = new StringBuilder();
        for (String name : mContactsNamesSelected) {
            names.append(name).append(":");
        }
        sendMes.setNames(names.toString().substring(0,names.length()-1));
        StringBuilder numbers = new StringBuilder();
        for (String number : mContactsNumbersSelected) {
            numbers.append(number).append(":");
        }
        sendMes.setNumbers(numbers.toString().substring(0, numbers.length() -1));
        return sendMes;
    }

    private void getContactData() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GETCONTACTS);
    }

    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    /**
     * 得到手机SIM卡联系人人信息
     **/
    private void getSIMContacts() {
        ContentResolver resolver = getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            mContactsNames.clear();
            mContactsNumbers.clear();
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);
                mContactsNames.add(contactName);
                mContactsNumbers.add(phoneNumber);
            }
            phoneCursor.close();
        }
        showSelectListView();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private CheckBox checkBox;
        private TextView telephone;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.id_tv_name);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            telephone = (TextView) itemView.findViewById(R.id.tele_number);
        }
    }

    final HashMap<Integer, Boolean> sign = new HashMap<>();
    int checkedTotal = 0;
    final DialogUtil dialogUtil = new DialogUtil();
    private int checkedCount = 0;

    private void showSelectListView() {
        checkedTotal = 0;
        if (sign.size() == 0) {
            for (int i = 0; i < mContactsNames.size(); i++) {
                sign.put(i, false);
            }
        } else {
            for (Map.Entry<Integer, Boolean> item : sign.entrySet()) {
                if (!item.getValue()) {
                    if (dialogUtil.allChecked == null) {
                        dialogUtil.allChecked.setChecked(false);
                        break;
                    } else {
                        dialogUtil.allChecked.setChecked(true);
                    }
                }
            }
//            if (dialogUtil.tv_total != null) {
//                dialogUtil.tv_total.setText("已选择" + checkedTotal + "个联系人");
//            }
        }
        dialogUtil.createDialog(SendMessageActivity.this, mAdapter = new RecyclerView.Adapter<MyViewHolder>() {
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                MyViewHolder holder = new MyViewHolder(LayoutInflater.from(SendMessageActivity.this).inflate(R.layout.item_dialog, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(final MyViewHolder holder, final int position) {
                holder.tv_name.setText(mContactsNames.get(position));
                holder.telephone.setText(mContactsNumbers.get(position));
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
                return mContactsNames.size();
            }
        });
        dialogUtil.fanxuan.setOnClickListener(new View.OnClickListener() {
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
        dialogUtil.allChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogUtil.allChecked.isChecked()) {
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
                dialogUtil.tv_total.setText("已选择" + checkedTotal + "个联系人");
                mAdapter.notifyDataSetChanged();
            }
        });
        dialogUtil.getDialogBuilder().setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeAllTags();
                mContactsNamesSelected.clear();
                mContactsNumbersSelected.clear();
                for (Map.Entry<Integer, Boolean> item : sign.entrySet()) {
                    if (item.getValue()) {
                        mContactsNamesSelected.add(mContactsNames.get(item.getKey()));
                        mContactsNumbersSelected.add(mContactsNumbers.get(item.getKey()));
                        addTag(mContactsNames.get(item.getKey()));
//                        Toast.makeText(SendMessageActivity.this, mContactsNames.get(item.getKey()) + mContactsNumbers.get(item.getKey()), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialogUtil.show();
    }

    private void invalideTotalCount() {
        checkedTotal = 0;
        for (Map.Entry<Integer, Boolean> item : sign.entrySet()) {
            if (item.getValue()) {
                checkedTotal++;
            }
        }
        dialogUtil.tv_total.setText("已选择" + checkedTotal + "个联系人");
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
            if (!dialogUtil.allChecked.isChecked()) {
                dialogUtil.allChecked.setChecked(true);
            }
        } else {
            if (dialogUtil.allChecked.isChecked()) {
                dialogUtil.allChecked.setChecked(false);
            }
        }
    }

    private void refreshData() {

    }

    private void initData() {
        Intent intent = getIntent();
        int festivalID = intent.getIntExtra(KEY_FESTIVALID, -1);
        int msgID = intent.getIntExtra(KEY_MSGID, -1);
        setData(festivalID, msgID);
    }

    private void setData(int festivalID, int msgID) {
        if (festivalID == -1)
            return;
        mFestival = DataLab.getInstance().getData().get(festivalID);
        setTitle(mFestival.getName());
        if (msgID != -1) {
            //选择了短信跳过来的
            for (Msg msg : DataLab.getInstance().getMSGList(festivalID)) {
                if (msg.getId() == msgID) {
                    id_et_content.setText(msg.getContent());
                }
            }
        } else {
            //用户自己编辑短信来发送
        }
    }

    private int mFestivalID;
    private int msgID;
    public static final String KEY_FESTIVALID = "festivalID";
    public static final String KEY_MSGID = "msgID";

    public static void toActivity(Context context, int festivalID, int msgID) {
        Intent intent = new Intent(context, SendMessageActivity.class);
        intent.putExtra(KEY_FESTIVALID, festivalID);
        intent.putExtra(KEY_MSGID, msgID);
        context.startActivity(intent);
    }

    //初始化view
    private void initViews() {
        mFlowLayout = (FlowLayout) findViewById(R.id.id_fl_contacts);
        id_loading_layout = (FrameLayout) findViewById(R.id.id_loading_layout);
        id_loading_layout.setVisibility(View.INVISIBLE);
        addPeople = (Button) findViewById(R.id.addPeople);
        sending_progress = (ProgressBar) findViewById(R.id.sending_progress);
        id_et_content = (EditText) findViewById(R.id.id_et_content);
        id_fab_send = (FloatingActionButton) findViewById(R.id.id_fab_send);
    }

}
