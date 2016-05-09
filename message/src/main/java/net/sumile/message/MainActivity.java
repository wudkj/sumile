package net.sumile.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.widget.TableLayout;

import net.sumile.message.fragment.FestivalCategoryFragment;
import net.sumile.message.fragment.MsgHistoryFragment;

public class MainActivity extends FragmentActivity {
    private TabLayout tableLayout;
    private ViewPager viewPager;
    public static String[] titles = new String[]{"节日短信", "发送记录"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        tableLayout = (TabLayout) findViewById(R.id.tableLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 1) return new MsgHistoryFragment();
                return new FestivalCategoryFragment();
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        tableLayout.setupWithViewPager(viewPager);
    }


}
