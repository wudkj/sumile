package net.sumile.sumile.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * Created by sumile on 2015/9/14.
 * 本程序版权归车联集团
 */
public class BaseActivity extends Activity {

    public void initView() {

    }

    public void initAction() {

    }

    public void initData() {

    }

    public void initShow() {

    }

    public LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mLayoutInflater = LayoutInflater.from(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
