package com.kyboon.kindlepusher;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kyboon.kindlepusher.CustomUI.LockableScrollView;
import com.kyboon.kindlepusher.DataTypes.Book;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BookSummaryActivity extends AppCompatActivity {

    private LockableScrollView outerScrollView;
    private NestedScrollView innerScrollView;
    private RelativeLayout informationContainer;

    private View fadeInView;
    private ImageView ivBookCover;
    private Button btnAdd;
    private TextView tvBookTitle;
    private TextView tvAuthor;
    private TextView tvCategory;
    private TextView tvChapterCount;
    private TextView tvDescription;
    private TextView tvLastUpdated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportPostponeEnterTransition();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_book_summary);

        outerScrollView = findViewById(R.id.outerScrollView);
        innerScrollView = findViewById(R.id.innerScrollView);
        informationContainer = findViewById(R.id.ContainerMainInformation);
        innerScrollView.setNestedScrollingEnabled(false);

        fadeInView = findViewById(R.id.fadeInView);
        ivBookCover = findViewById(R.id.ivBookCover);
        btnAdd = findViewById(R.id.btnAdd);
//        btnAdd = findViewById()
        tvBookTitle = findViewById(R.id.tvBookTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvCategory = findViewById(R.id.tvBookCategory);
        tvChapterCount = findViewById(R.id.tvChapterCount);
        tvDescription = findViewById(R.id.tvBookDescription);
        tvLastUpdated = findViewById(R.id.tvLastUpdated);

        Book book = getIntent().getParcelableExtra("BOOK");
        if (book != null) {
            tvBookTitle.setText(book.title);
            tvAuthor.setText(book.author);
            tvCategory.setText(book.majorCate);
            tvChapterCount.setText("Total Chapters: " + book.chaptersCount);
            tvDescription.setText(book.getLongIntro());
            String updatedString = "Last Updated: " + book.lastChapter + " at " + book.updated;
            tvLastUpdated.setText(updatedString);

            Uri uri = Uri.parse(book.cover);
//            Picasso.get().load(uri.getLastPathSegment()).fit().into(ivBookCover);
            supportPostponeEnterTransition();
            Picasso.get().load(uri.getLastPathSegment())
                    .noFade()
                    .fit()
                    .into(ivBookCover, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                            setUpScrollViews();
                        }

                        @Override
                        public void onError(Exception e) {
                            supportStartPostponedEnterTransition();
                        }
                    });
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpScrollViews();
            }
        });

        setUpScrollViews();
    }

    private void setUpScrollViews() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        final int actionBarSize = (int) (styledAttributes.getDimension(0, 0) / getResources().getDisplayMetrics().density);
        styledAttributes.recycle();

        innerScrollView.getLayoutParams().height = height - actionBarSize - informationContainer.getHeight();

        ViewTreeObserver.OnScrollChangedListener scrollListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int distance = informationContainer.getHeight() - actionBarSize;
                float distancePercentage = (float)(outerScrollView.getScrollY()) / (float) distance;
                fadeInView.setAlpha(Math.min(distancePercentage, 1.0f));

                if (outerScrollView.getChildAt(0).getBottom() <= (outerScrollView.getHeight() + outerScrollView.getScrollY()))
                    innerScrollView.setNestedScrollingEnabled(true);
                else
                    innerScrollView.setNestedScrollingEnabled(false);

                if (innerScrollView.getScrollY() == 0)
                    outerScrollView.setScrollingEnabled(true);
                else
                    outerScrollView.setScrollingEnabled(false);
            }
        };

        outerScrollView.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
        innerScrollView.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
    }

//    private void getBookSummary(String id) {
//        ApiHelper.getInstance().getBook(id, new ApiHelperCallback<Book>() {
//            @Override
//            public void onResult(Book result) {
//                Uri uri = Uri.parse(result.cover);
//                Picasso.get().load(uri.getLastPathSegment()).fit().into(ivBookCover);
//                tvBookTitle.setText(result.title);
//                tvAuthor.setText(result.author);
//                tvCategory.setText(result.majorCate);
//                tvChapterCount.setText("Total Chapters: " + result.chaptersCount);
//                tvDescription.setText(result.getLongIntro());
//                String updatedString = "Last Updated: " + result.lastChapter + " at " + result.updated;
//                tvLastUpdated.setText(updatedString);
//                Log.d("debuggg", result.getLongIntro());
//                setUpScrollViews();
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
