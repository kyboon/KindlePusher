package com.kyboon.kindlepusher;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.kyboon.kindlepusher.CustomUI.LockableScrollView;
import com.kyboon.kindlepusher.DataAdapters.ChapterAdapter;
import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.DataTypes.BookSource;
import com.kyboon.kindlepusher.DataTypes.ChapterSource;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

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

    private Button btnSort;
    private Spinner spinnerSource;
    private RecyclerView rvChapters;
    private ProgressBar progressBar;

    private String id;
    private List<BookSource> sources;
    private  ChapterAdapter chapterAdapter;

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
        tvBookTitle = findViewById(R.id.tvBookTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvCategory = findViewById(R.id.tvBookCategory);
        tvChapterCount = findViewById(R.id.tvChapterCount);
        tvDescription = findViewById(R.id.tvBookDescription);
        tvLastUpdated = findViewById(R.id.tvLastUpdated);
        btnSort = findViewById(R.id.btnSort);
        spinnerSource = findViewById(R.id.spinnerSource);
        rvChapters = findViewById(R.id.rvChapters);
        progressBar = findViewById(R.id.progress_circular);

        rvChapters.setLayoutManager( new LinearLayoutManager(this));
        chapterAdapter = new ChapterAdapter(new ChapterAdapter.IChapterAdapter() {
            @Override
            public void selectedChapter(String id, String link) {
                Log.d("debuggg", "selected Chapter: " + id);
            }

            @Override
            public void longPressedChapter(String id, String link) {
                Log.d("debuggg", "long pressed Chapter: " + id);
            }
        });
        rvChapters.setAdapter(chapterAdapter);

        Book book = getIntent().getParcelableExtra("BOOK");
        if (book != null) {
            id = book.id;
            tvBookTitle.setText(book.title);
            tvAuthor.setText(book.author);
            String categoryString = "";
            if (book.majorCateV2 != null)
                categoryString = book.majorCateV2 + " ";
            if (book.minorCateV2 != null)
                categoryString += book.minorCateV2;
            tvCategory.setText(categoryString);
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

            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wasAscending = btnSort.getScaleY() == -1;
                btnSort.setScaleY(wasAscending ? 1 : -1);
                chapterAdapter.setAscending(!wasAscending);
            }
        });

        setUpScrollViews();

        getSources();
    }

    private void setUpScrollViews() {
        Display display = getWindowManager().getDefaultDisplay();
        Point windowSize = new Point();
        display.getSize(windowSize);
        int windowHeight = windowSize.y;

        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        final int actionBarSize = (int) (styledAttributes.getDimension(0, 0) / getResources().getDisplayMetrics().density);
        styledAttributes.recycle();

        rvChapters.getLayoutParams().height = windowHeight - actionBarSize - informationContainer.getHeight();

        innerScrollView.getLayoutParams().height = windowHeight - actionBarSize - informationContainer.getHeight();

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

                if (innerScrollView.getChildAt(0).getBottom() <= (innerScrollView.getHeight() + innerScrollView.getScrollY()))
                    rvChapters.setNestedScrollingEnabled(true);
                else
                    rvChapters.setNestedScrollingEnabled(false);
            }
        };

        outerScrollView.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
        innerScrollView.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
        rvChapters.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
    }

    private void getSources() {
        if (id == null)
            return;

        progressBar.setVisibility(View.VISIBLE);
        ApiHelper.getInstance().getBookSource(id, new ApiHelperCallback<List<BookSource>>() {
            @Override
            public void onResult(List<BookSource> result) {
                sources = result;
                progressBar.setVisibility(View.GONE);
                setSpinnerSource();
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setSpinnerSource() {
        final List<String> source_names = new ArrayList<>();
        for (BookSource source: sources) {
            source_names.add(source.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, source_names);
        spinnerSource.setAdapter(adapter);
        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getChapterSources(sources.get(position)._id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getChapterSources(String sourceId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiHelper.getInstance().getChapterSources(sourceId, new ApiHelperCallback<List<ChapterSource>>() {
            @Override
            public void onResult(List<ChapterSource> result) {
                setChaptersRV(result);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setChaptersRV(List<ChapterSource> chapterSources) {
        chapterAdapter.setChapterList(chapterSources);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
