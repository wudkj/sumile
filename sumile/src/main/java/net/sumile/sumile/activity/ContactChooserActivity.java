package net.sumile.sumile.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import net.sumile.sumile.base.BaseActivity;
import net.sumile.sumile.base.BaseListActivity;
import net.sumile.sumile.base.Constants;
import net.sumile.sumile.bean.DescribeAndCode;
import net.sumile.sumile.util.ContactsChooserUtil;
import net.sumile.sumile.util.DialogUtil;
import net.sumile.sumile.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by sumile on 2016/5/8.
 */
public class ContactChooserActivity extends BaseListActivity {
    private ArrayList<DescribeAndCode> data = new ArrayList<DescribeAndCode>();
    private Context mContext;

    @Override
    public boolean ifUsingDefault() {
        return true;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
        data.add(new DescribeAndCode("获取联系人信息:方法1", "Intent localIntent = new Intent(\"android.intent.action.PICK\");\n" +
                "localIntent.setType(\"vnd.android.cursor.dir/phone\");\n" +
                "mActivity.startActivityForResult(localIntent, REQUEST_0);"));
        data.add(new DescribeAndCode("获取联系人信息:方法2", "Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);\n" +
                "startActivityForResult(intent, REQUEST_1);"));
        data.add(new DescribeAndCode("获取联系人信息:自定义view显示所有联系人，通过勾选来选择联系人。", "代码太多了，查看util下的ContactsChooserUtil"));
        initDefaultRecycleView(data, new DefaultRecycleViewInterface() {
            @Override
            public void onItemClick(View tv, int position) {
                switch (position) {
                    case 0:
                        getUserInfo1();
                        break;
                    case 1:
                        getUserInfo2();
                        break;
                    case 2:
                        getUserInfo3();
                        break;
                }
            }


            @Override
            public void onLongClickToShowCode(View tv, int position) {
                DialogUtil.showTextDialog(mContext, data.get(position).getCode());
            }

            @Override
            public String setHeaderTitle() {
                return "联系人信息获取";
            }

            @Override
            public String setHeaderGitHubUrl() {
                return Constants.urlList.get("ContactChooserActivity");
            }

        });
    }

    String content = "";

    private void getUserInfo3() {
        content = "";
        new ContactsChooserUtil(mContext, ContactChooserActivity.this, new ContactsChooserUtil.ContactsChooserImpl() {
            @Override
            public void onContactsChoosed(ArrayList<String> mContactsNames, ArrayList<String> mContactsNumbers) {
                for (int i = 0; i < mContactsNames.size(); i++) {
                    content += "姓名:" + mContactsNames.get(i) + "   电话:" + mContactsNumbers.get(i) + "\n";
                }
                DialogUtil.showTextDialog(mContext, content.toString());
            }
        });
    }

    private void getUserInfo2() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_1);
    }

    private void getUserInfo1() {
        Intent localIntent = new Intent("android.intent.action.PICK");
        localIntent.setType("vnd.android.cursor.dir/phone");
        startActivityForResult(localIntent, REQUEST_0);
    }

    private static final int REQUEST_0 = 0;
    private static final int REQUEST_1 = 1;
    String disPlayName;
    String phoneNumber;

    protected void onActivityResult(int paramInt1, int resultCode, Intent paramIntent) {
        if (paramIntent == null) {
            return;
        }
        switch (paramInt1) {
            case REQUEST_0:
                if (resultCode == RESULT_OK) {
                    Uri localUri = paramIntent.getData();
                    if (localUri != null) {
                        Cursor localCursor = getContentResolver().query(localUri, null, null, null, null);
                        if (localCursor != null)
                            while (true) {
                                if (!localCursor.moveToNext()) {
                                    DialogUtil.showTextDialog(mContext, this.disPlayName + "\n" + this.phoneNumber);
                                    super.onActivityResult(paramInt1, resultCode, paramIntent);
                                    return;
                                }
                                int i = localCursor.getColumnIndex("display_name");
                                if (i >= 0) {
                                    this.disPlayName = localCursor.getString(i);
                                    int j = localCursor.getColumnIndex("data1");
                                    if (j >= 0)
                                        this.phoneNumber = localCursor.getString(j);
                                }
                            }
                    }
                }
                break;
            case REQUEST_1:
                if (resultCode == RESULT_OK) {
                    Uri uri = paramIntent.getData();
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String number = getContactNumber(cursor);
                    DialogUtil.showTextDialog(mContext, contactName + "\n" + number);
                }
                break;
        }
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
}
