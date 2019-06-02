package com.kyboon.kindlepusher;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 2:
                        return new StoreFragment();
                    case 3:
                        return new RankingFragment();
                    default:
                        return new BookshelfFragment();
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_shelf);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_store);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_hot);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_settings);
        tabLayout.setTabIconTintResource(R.color.secondaryLightColor);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            viewPager.setCurrentItem(0);
    }

}
