package net.sumile.sumile.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/11.
 */
public class NumberPickerCreater extends AlertDialog implements NumberPicker.OnValueChangeListener, NumberPicker.Formatter, NumberPicker.OnScrollListener, DialogInterface.OnClickListener {

    protected NumberPickerCreater(Context context) {
        super(context);
    }

//    protected NumberPickerCreater(Context context, boolean cancelable, OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }
//
//    protected NumberPickerCreater(Context context, int themeResId) {
//        this(context, true, null);
//    }

    @Override
    public String format(int value) {
        return null;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    public interface OnTimeSetListener {

        void onTimeSet(ArrayList<NumberPickerBean> numberPickerBeans);

    }

    public static class NumberPickerBean {
        private NumberPicker numberPicker;
        private String[] numberPickerValues;
        private boolean numberPickerType;
        private String numberPickerSelectedValue;

        public String getSelectedValue() {
            return numberPickerSelectedValue;
        }

        public void setSelectedValue(String selectedValue) {
            this.numberPickerSelectedValue = selectedValue;
        }

        public NumberPickerBean(NumberPicker mNumberPicker, String[] mNumberPickerValues, boolean mNumberPickerType) {
            this.numberPicker = mNumberPicker;
            this.numberPickerValues = mNumberPickerValues;
            this.numberPickerType = mNumberPickerType;
        }

        public NumberPicker getmNumberPicker() {
            return numberPicker;
        }

        public void setmNumberPicker(NumberPicker mNumberPicker) {
            this.numberPicker = mNumberPicker;
        }

        public String[] getmNumberPickerValues() {
            return numberPickerValues;
        }

        public void setmNumberPickerValues(String[] mNumberPickerValues) {
            this.numberPickerValues = mNumberPickerValues;
        }

        public boolean getmNumberPickerType() {
            return numberPickerType;
        }

        public void setmNumberPickerType(boolean mNumberPickerType) {
            this.numberPickerType = mNumberPickerType;
        }
    }

    public static class Builder {
        private Context mContext;
        private View mRootView;
        private ArrayList<NumberPickerBean> mBeans = new ArrayList<NumberPickerBean>();
        private OnTimeSetListener mCallback;
        private NumberPickerCreater numberPickerCreater;

        public Builder(Context context, View rootView, OnTimeSetListener callback) {
            if (context == null || rootView == null) {
                return;
            }
            this.mContext = context;
            mRootView = rootView;
            mCallback = callback;
        }

        public Builder addNumberPicker(NumberPicker numberPicker, String[] displayValues) {
            numberPicker.setDisplayedValues(displayValues);
            numberPicker.setMaxValue(displayValues.length - 1);
            numberPicker.setMinValue(0);
            numberPicker.setValue(0);
            numberPicker.setWrapSelectorWheel(true);
            numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numberPicker.clearFocus();
            NumberPickerBean bean = new NumberPickerBean(numberPicker, displayValues, true);
            mBeans.add(bean);
            return this;
        }

        public Builder addNumberPicker(NumberPicker numberPicker, int minValue, int maxValue) {
            numberPicker.setMaxValue(maxValue);
            numberPicker.setMinValue(minValue);
            numberPicker.setValue(minValue);
            numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numberPicker.clearFocus();
            int count = maxValue - minValue + 1;
            String[] displayValues = new String[Math.abs(count)];
            for (int i = 0; i < count; i++) {
                displayValues[i] = String.valueOf(minValue++);
            }
            NumberPickerBean bean = new NumberPickerBean(numberPicker, displayValues, false);
            mBeans.add(bean);
            return this;
        }

        public Builder build() {
            numberPickerCreater = new NumberPickerCreater(mContext);
            numberPickerCreater.setIcon(0);
            numberPickerCreater.setTitle("");
            numberPickerCreater.setView(mRootView);
            numberPickerCreater.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ArrayList<String> resultValues = new ArrayList<String>();
                    for (int i = 0; i < mBeans.size(); i++) {
                        NumberPickerBean mBean = mBeans.get(i);
                        NumberPicker mNumberPicker = mBean.getmNumberPicker();
                        if (mNumberPicker != null) {
                            mNumberPicker.clearFocus();
                            if (mBean.getmNumberPickerType()) {
                                resultValues.add(mBean.getmNumberPickerValues()[mNumberPicker.getValue()]);
                                mBean.setSelectedValue(mBean.getmNumberPickerValues()[mNumberPicker.getValue()]);
                            } else {
                                resultValues.add(String.valueOf(mNumberPicker.getValue()));
                                mBean.setSelectedValue(String.valueOf(mNumberPicker.getValue()));
                            }
                        }
                    }
                    if (mCallback != null) {
                        mCallback.onTimeSet(mBeans);
                    } else {
                        numberPickerCreater.dismiss();
                    }
                }
            });
            return this;
        }

        public void show() {
            if (numberPickerCreater != null) {
                numberPickerCreater.show();
            }
        }

    }

}
