package com.kyboon.kindlepusher;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tabLayout);
//        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0)
                    return new BookshelfFragment();
                else
                    return new BookstoreFragment();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0)
                    return "bookshelf";
                else
                    return "bookstore";
            }
        });
        tabLayout.setupWithViewPager(viewPager);



        //getBook();
        //getChapter("http://www.snwx.com/book/15/15231/4307839.html");
//        ApiHelper.getInstance().search("地下城", new ApiHelperCallback<String>() {
//            @Override
//            public void onResult(String result) {
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            viewPager.setCurrentItem(0);
    }

//    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//        public ScreenSlidePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            if (position == 0)
//                return new BookshelfFragment();
//            else
//                return new BookstoreFragment();
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//    }

}
