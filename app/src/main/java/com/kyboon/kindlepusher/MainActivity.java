package com.kyboon.kindlepusher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.kyboon.kindlepusher.DataTypes.Bookmark;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static CollectionReference bookshelfReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final int RC_SIGN_IN = 666;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        return new HomeFragment();
                    case 1:
                        return new ShelfFragment();
                    case 2:
                        return new StoreFragment();
                    case 3:
                        return new RankingFragment();
                    case 4:
                        return new SettingsFragment();
                    default:
                        return new ShelfFragment();
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_shelf);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_store);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_hot);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_settings);
        tabLayout.setTabIconTintResource(R.color.secondaryLightColor);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onPageSelected(int i) {
                getSupportActionBar().setShowHideAnimationEnabled(false);
                if (i == 0)
                    getSupportActionBar().hide();
                else
                    getSupportActionBar().show();
                switch (i) {
                    case 1:
                        setTitle("Bookshelf");
                        break;
                    case 2:
                        setTitle("Bookstore");
                        break;
                    case 3:
                        setTitle("Rankings");
                        break;
                    case 4:
                        setTitle("Settings");
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    viewPager.setCurrentItem(0);
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            viewPager.setCurrentItem(0);
    }

    private void saveBookmark(Bookmark bookmark) {
        bookshelfReference.document().set(bookmark);
    }

}
