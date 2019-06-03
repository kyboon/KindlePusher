package com.kyboon.kindlepusher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.DataTypes.Bookmark;
import com.kyboon.kindlepusher.DataTypes.Bookshelf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
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

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build());

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
        viewPager.setOffscreenPageLimit(3);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_shelf);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_store);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_hot);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_settings);
        tabLayout.setTabIconTintResource(R.color.secondaryLightColor);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
//                    documentReference = firebaseFirestore.collection("Users").document(user.getUid()).collection("Data").document("Bookshelf");
////                    Map<String, Object> dataToStore = new HashMap<>();
//                    List<Bookmark> bookmarkList = new ArrayList<>();
//                    bookmarkList.add(new Bookmark("Help222","ididi324d"));
//                    bookmarkList.add(new Bookmark("Help5656","idid12341id2"));
//                    Bookshelf bookshelf = new Bookshelf();
//                    bookshelf.setBookmarks(bookmarkList);
////                    dataToStore.put("Bookshelf", bookshelf);
////                    documentReference.set(dataToStore);
//                    documentReference.set(bookshelf);
                    bookshelfReference = firebaseFirestore.collection("Users").document(user.getUid()).collection("Bookshelf");
//                    saveBookmark(new Bookmark("Test1", "test2"));
                } else {
                    documentReference = null;
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
