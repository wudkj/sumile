package net.sumile.message.util;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import net.sumile.message.data.DB_MSG;

/**
 * Created by Administrator on 2016/4/20.
 */
public class SMS_ContentProvider extends ContentProvider {
    public static final String AUTHORITY = "net.sumile.sms.provider.SmsProvider";
    public static final Uri URI_SMS_ALL = Uri.parse("content://" + AUTHORITY + "/sms");
    private static UriMatcher mMacher;
    public static final int SMS_ALL = 0;
    public static final int SMS_ONE = 1;
    public DB_Sms_Hepler mHelper;
    public SQLiteDatabase mDb;

    static {
        mMacher = new UriMatcher(UriMatcher.NO_MATCH);
        mMacher.addURI(AUTHORITY, "sms", SMS_ALL);
        mMacher.addURI(AUTHORITY, "sms/#", SMS_ONE);
    }

    @Override
    public boolean onCreate() {
        mHelper = DB_Sms_Hepler.getHelperInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = mMacher.match(uri);
        switch (match) {
            case SMS_ONE:
                long id = ContentUris.parseId(uri);
                selection = "_id = ?";
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            case SMS_ALL:
                break;
            default:
                throw new IllegalArgumentException("uri错误");
        }
        mDb = mHelper.getReadableDatabase();
        Cursor c = mDb.query(DB_MSG.TABNAME, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), URI_SMS_ALL);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = mMacher.match(uri);
        switch (match) {
            case SMS_ALL:
                mDb = mHelper.getWritableDatabase();
                long rowId = mDb.insert(DB_MSG.TABNAME, null, values);
                if (rowId > 0) {
                    notifyDataSetChanged();
                    return ContentUris.withAppendedId(URI_SMS_ALL, rowId);
                }
                break;
            default:
                throw new IllegalArgumentException("uri参数错误");
        }
        return null;
    }

    private void notifyDataSetChanged() {
        getContext().getContentResolver().notifyChange(URI_SMS_ALL, null);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
