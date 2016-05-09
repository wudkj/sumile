package net.sumile.sumile.base;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import net.sumile.sumile.R;
import net.sumile.sumile.bean.DescribeAndCode;
import net.sumile.sumile.view.Header;
import net.sumile.sumile.view.HorizontalDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/8.
 */
public abstract class BaseListActivity extends BaseActivity {
    private RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (ifUsingDefault()) {
            initView();
        } else {
        }
        mHeader = (Header) findViewById(R.id.header);
    }

    private Header mHeader;

    public abstract boolean ifUsingDefault();

    @Override
    public void initView() {
        setContentView(R.layout.listlayout);
        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);

    }

    public void initDefaultRecycleView(ArrayList<DescribeAndCode> strArraylist, DefaultRecycleViewInterface listener) {
        RecyclerView.Adapter adapter = new DefaultRecycleViewAdapter(strArraylist, listener);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(adapter);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(ContextCompat.getColor(this, R.color.baseColor))
                .build());
    }

    class DefaultRecycleViewAdapter extends RecyclerView.Adapter<DefaultRecycleViewHolder> {
        private ArrayList<DescribeAndCode> strs;
        private DefaultRecycleViewInterface listener;

        public DefaultRecycleViewAdapter(ArrayList<DescribeAndCode> strs, DefaultRecycleViewInterface listener) {
            if (strs == null) {
                strs = new ArrayList<>();
            }
            this.strs = strs;
            if (listener != null) {
                this.listener = listener;
            }
        }

        @Override
        public DefaultRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            DefaultRecycleViewHolder holder = new DefaultRecycleViewHolder(LayoutInflater.from(BaseListActivity.this).inflate(R.layout.list_textview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(DefaultRecycleViewHolder holder, final int position) {
            holder.tv.setText(strs.get(position).getDescribe());
            if (listener != null) {
                mHeader.setData(listener.setHeaderTitle(), listener.setHeaderGitHubUrl());
                holder.tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(v, position);
                    }
                });
                holder.tv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        listener.onLongClickToShowCode(v, position);
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return strs.size();
        }
    }

    public interface DefaultRecycleViewInterface {
        void onItemClick(View tv, int position);

        void onLongClickToShowCode(View tv, int position);

        String setHeaderTitle();

        String setHeaderGitHubUrl();
    }

    class DefaultRecycleViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public DefaultRecycleViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.id_listTextView);
        }
    }
}
