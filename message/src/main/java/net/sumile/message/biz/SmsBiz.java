package net.sumile.message.biz;

import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.SmsManager;

import net.sumile.message.data.DB_MSG;
import net.sumile.message.util.SMS_ContentProvider;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Created by zhangxiaokang on 16/4/14.
 */
public class SmsBiz {
    private Context context;

    public SmsBiz(Context context) {
        this.context = context;
    }

    /**
     * @param number     要发送的电话号码
     * @param msg        要发送的内容
     * @param sendPI     当短信发出时，成功的话sendIntent会把其内部的描述的intent广播出去，否则产生错误代码并通过android.app.PendingIntent.OnFinished进行回调，这个参数最好不为空，否则会存在资源浪费的潜在问题；
     * @param deliveryPI 是当消息已经传递给收信人后所进行的PendingIntent广播。
     * @return 发送的条数(因为可能文字过长，那么会分开发送)
     */
    public int sendMsg(String number, String msg, PendingIntent sendPI, PendingIntent deliveryPI) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> contents = smsManager.divideMessage(msg);
        for (String content : contents) {
            if (number.equals("10086") || number.equals("1008611") || number.equals("18730654031")) {
                smsManager.sendTextMessage(number, null, content, sendPI, deliveryPI);
            }
        }
        return contents.size();
    }

    /**
     * @param numbers    准备发送的号码的列表
     * @param msg        要发送的信息
     * @param sendPI     参见{@link #sendMsg(String, String, PendingIntent, PendingIntent)} }
     * @param deliveryPI 参见{@link #sendMsg(String, String, PendingIntent, PendingIntent)} }
     * @return 总共发送的条数
     */
    public int sendMsg(ArrayList<String> numbers, DB_MSG msg, PendingIntent sendPI, PendingIntent deliveryPI) {
        save(msg);
        int count = 0;
        for (String phoneNumber : numbers) {
            count += sendMsg(phoneNumber, msg.getMsg(), sendPI, deliveryPI);
        }
        return count;
    }

    private void save(DB_MSG msg) {
        msg.setDate(new Date());
        ContentValues values = new ContentValues();
        values.put(DB_MSG.COLUMN_DATE, msg.getDate().getTime());
        values.put(DB_MSG.COLUMN_DATE_STR, msg.getDateStr());
        values.put(DB_MSG.COLUMN_FESTIVAL_NAME, msg.getFestivalName());
        values.put(DB_MSG.COLUMN_MSG, msg.getMsg());
        values.put(DB_MSG.COLUMN_NAMES, msg.getNames());
        values.put(DB_MSG.COLUMN_NUMBERS, msg.getNumbers());
        context.getContentResolver().insert(SMS_ContentProvider.URI_SMS_ALL, values);
    }
}
