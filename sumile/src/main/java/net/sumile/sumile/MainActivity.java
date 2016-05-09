package net.sumile.sumile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.tv.TvContentRating;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sumile.sumile.adapter.MainAdapter;
import net.sumile.sumile.base.BaseActivity;
import net.sumile.sumile.base.Constants;
import net.sumile.sumile.bean.MainData;
import net.sumile.sumile.data.MainDataProvider;
import net.sumile.sumile.util.DialogUtil;
import net.sumile.sumile.util.IntentHelper;
import net.sumile.sumile.util.ToastUtil;
import net.sumile.sumile.view.DividerItemDecoration;
import net.sumile.sumile.view.HorizontalDividerItemDecoration;
import net.sumile.sumile.view.RecycleViewDivider;
import net.sumile.sumile.view.VerticalDividerItemDecoration;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ImageView logo;
    private RecyclerView mRecycleView;
    private Context mContext;
    private MainAdapter mAdapter;
    private TextView title;
    private ArrayList<MainData> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        initView();
        initData();
        initAction();
        initShow();
    }

    @Override
    public void initView() {
        title = (TextView) findViewById(R.id.id_title);
        logo = (ImageView) findViewById(R.id.id_github);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openInWebView(mContext, Constants.BLOGURL);
            }
        });
        mRecycleView = (RecyclerView) findViewById(R.id.id_mainRecycleView);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(manager);
    }

    @Override
    public void initData() {
        mData = MainDataProvider.getMainDatas();
        mAdapter = new MainAdapter(mContext, mData);
    }

    @Override
    public void initAction() {
        mAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(mContext, mData.get(position).getClz());
                mContext.startActivity(intent);
            }
        });

        title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToastUtil.showShortToast(mContext, "热爱改变生活");
                return true;
            }
        });
    }

    @Override
    public void initShow() {
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(ContextCompat.getColor(mContext, R.color.baseColor))
                .build());
    }


}
