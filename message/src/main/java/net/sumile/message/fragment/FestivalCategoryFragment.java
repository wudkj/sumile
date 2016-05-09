package net.sumile.message.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import net.sumile.message.R;
import net.sumile.message.data.DataLab;
import net.sumile.message.data.Festival;

import java.util.ArrayList;

/**
 * Created by zhangxiaokang on 16/4/12.
 */
public class FestivalCategoryFragment extends Fragment {
    private LayoutInflater mLayoutInflater;
    private GridView mGridView;
    private ArrayList<Festival> mData;
    private ArrayAdapter<Festival> mAdapter;
    public static String INTENT_NAME = "festivalID";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.festival_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mLayoutInflater = LayoutInflater.from(getActivity());
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter = new ArrayAdapter<Festival>(getActivity(), -1, DataLab.getInstance().getData()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = mLayoutInflater.inflate(R.layout.festival_textview, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.tv_festival);
                tv.setText(getItem(position).getName());
                return convertView;
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FestivalChooserActivity.class);
                intent.putExtra(INTENT_NAME, mAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }
}
