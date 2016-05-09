package net.sumile.sumile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sumile.sumile.R;
import net.sumile.sumile.bean.MainData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/28.
 */
public class MainAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context mContext;
    private ArrayList<MainData> mData = new ArrayList<>();
    private OnItemClickListener mListener;

    public MainAdapter(Context mContext, ArrayList<MainData> mData) {
        this.mContext = mContext;
        this.mData.addAll(mData);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (mListener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v, position);
                }
            });
        }
        MainData data = mData.get(position);
        holder.title.setText(data.getTitle());
        holder.desc.setText(data.getDesc());
        holder.date.setText(data.getDate());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView desc;
    public TextView date;
    public View view;

    public MyViewHolder(View view) {
        super(view);
        this.view = view;
        title = (TextView) view.findViewById(R.id.id_contentTitle);
        desc = (TextView) view.findViewById(R.id.id_contentDesc);
        date = (TextView) view.findViewById(R.id.id_contentDate);
    }
}
