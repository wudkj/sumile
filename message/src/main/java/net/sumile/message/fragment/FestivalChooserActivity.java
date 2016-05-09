package net.sumile.message.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.sumile.message.R;
import net.sumile.message.SendMessageActivity;
import net.sumile.message.data.DataLab;
import net.sumile.message.data.Msg;

/**
 * Created by zhangxiaokang on 16/4/13.
 */
public class FestivalChooserActivity extends FragmentActivity {
    private static final String TAG = "sumile";
    private ListView mListView;
    private FloatingActionButton mFabToSend;
    private ArrayAdapter<Msg> mAdapter;
    private int mFestivalId;
    private LayoutInflater mLayoutInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_msg);
        mLayoutInflater = LayoutInflater.from(this);
        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        mFestivalId = intent.getIntExtra(FestivalCategoryFragment.INTENT_NAME, -1);
        Log.i(TAG, "initViews: " + mFestivalId);
        mListView = (ListView) findViewById(R.id.id_lv_msgs);
        mFabToSend = (FloatingActionButton) findViewById(R.id.id_fab_send);
        mFabToSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageActivity.toActivity(FestivalChooserActivity.this, mFestivalId, -1);
            }
        });
        mListView.setAdapter(mAdapter = new ArrayAdapter<Msg>(this, -1, DataLab.getInstance().getMSGList(mFestivalId)) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = mLayoutInflater.inflate(R.layout.item_msg, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.item_tv);
                tv.setText(getItem(position).getContent());
                Button toSend = (Button) convertView.findViewById(R.id.id_btn_send);
                toSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendMessageActivity.toActivity(FestivalChooserActivity.this, mFestivalId, getItem(position).getId());
                    }
                });
                return convertView;
            }
        });
    }
}
