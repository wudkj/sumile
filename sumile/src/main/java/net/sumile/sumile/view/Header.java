package net.sumile.sumile.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sumile.sumile.R;
import net.sumile.sumile.base.Constants;
import net.sumile.sumile.util.IntentHelper;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Header extends RelativeLayout implements View.OnClickListener {
    public Header(Context context) {
        this(context, null);
    }

    public Header(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Header(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private ImageView back;
    private ImageView github;
    private TextView title;
    private Context mContext;
    //values
    private String mUrl = "";
    private String mTitle = "";

    private void initView(Context context) {
        mContext = context;
        View.inflate(context, R.layout.header, this);
        back = (ImageView) findViewById(R.id.id_back);
        github = (ImageView) findViewById(R.id.id_github);
        title = (TextView) findViewById(R.id.id_title);
        initAction();
    }

    public void setData(String mTitle, String mUrl) {
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = "sumile";
        }
        this.mTitle = mTitle;
        title.setText(mTitle);
        if (TextUtils.isEmpty(mUrl)) {
            github.setImageResource(R.drawable.newlogo_white);
            this.mUrl = Constants.BLOGURL;
        } else {
            this.mUrl = mUrl;
            if (mUrl.contains("sumile")) {
                github.setImageResource(R.drawable.newlogo_white);
            }
        }
    }

    private void initAction() {
        back.setOnClickListener(this);
        github.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                ((Activity) mContext).finish();
                break;
            case R.id.id_github:
                IntentHelper.openInWebView(mContext, mUrl);
                break;
        }
    }

    public String getUrl() {
        return mUrl;
    }

    public String getTitle() {
        return mTitle;
    }
}
