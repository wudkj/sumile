package net.sumile.message.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import net.sumile.message.R;
import net.sumile.message.data.DB_MSG;
import net.sumile.message.util.SMS_ContentProvider;
import net.sumile.message.view.FlowLayout;

/**
 * Created by Administrator on 2016/4/25.
 */
public class MsgHistoryFragment extends ListFragment {
    private LayoutInflater mInflate;
    private CursorAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInflate = LayoutInflater.from(getActivity());
        initLoader();
        setupListAdapter();
    }

    private static final int LOADER_ID = 1;

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cursor = new CursorLoader(getActivity(), SMS_ContentProvider.URI_SMS_ALL, null, null, null, null);
                return cursor;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (loader.getId() == LOADER_ID) {
                    mAdapter.swapCursor(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdapter.swapCursor(null);
            }
        });
    }

    private void setupListAdapter() {
        mAdapter = new CursorAdapter(getActivity(), null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = mInflate.inflate(R.layout.item_sended_msg, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tv_content = (TextView) view.findViewById(R.id.id_sended_content);
                TextView tv_date = (TextView) view.findViewById(R.id.id_sended_date);
                FlowLayout fl = (FlowLayout) view.findViewById(R.id.id_sended_fl);
                tv_content.setText(cursor.getString(cursor.getColumnIndex(DB_MSG.COLUMN_MSG)));
                tv_date.setText(cursor.getString(cursor.getColumnIndex(DB_MSG.COLUMN_DATE_STR)));
                String names = cursor.getString(cursor.getColumnIndex(DB_MSG.COLUMN_NAMES));
                String nameData[] = names.split(":");
                for (String name : nameData) {
                    addTag(fl, name);
                }
            }
        };
        setListAdapter(mAdapter);
    }

    private void addTag(FlowLayout fl, String name) {
        TextView tv = (TextView) mInflate.inflate(R.layout.bg, fl, false);
        tv.setText(name);
        fl.addView(tv);
    }

}
