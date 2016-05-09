package net.sumile.message.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.sumile.message.data.DB_MSG;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/4/20.
 */
public class DB_Sms_Hepler extends SQLiteOpenHelper {
    private static String DB_NAME = "db_sms";
    private static int DB_VERSION = 1;

    private DB_Sms_Hepler(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    private static DB_Sms_Hepler mHelper;

    public static DB_Sms_Hepler getHelperInstance(Context context) {
        if (mHelper == null) {
            synchronized (DB_Sms_Hepler.class) {
                if (mHelper == null) {
                    mHelper = new DB_Sms_Hepler(context);
                }
            }
        }
        return mHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + DB_MSG.TABNAME + " ( " +
                " _id integer primary key autoincrement , " +
                DB_MSG.COLUMN_DATE + " integer , " +
                DB_MSG.COLUMN_MSG + " text , " +
                DB_MSG.COLUMN_FESTIVAL_NAME + " text ," +
                DB_MSG.COLUMN_NAMES + " text , " +
                DB_MSG.COLUMN_DATE_STR + " text , " +
                DB_MSG.COLUMN_NUMBERS + " text )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
