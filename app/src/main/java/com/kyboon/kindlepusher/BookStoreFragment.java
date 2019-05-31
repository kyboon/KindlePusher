package com.kyboon.kindlepusher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.kyboon.kindlepusher.DataAdapters.BookAdapter;
import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.DataTypes.Chapter;
import com.kyboon.kindlepusher.DataTypes.ChapterSource;

import java.util.List;

public class BookStoreFragment extends Fragment implements BookAdapter.IBookAdapter {
    BookAdapter bookAdapter;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bookstore, container, false);

        progressBar = rootView.findViewById(R.id.progress_circular);

        RecyclerView recyclerView = rootView.findViewById(R.id.rvBookstore);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookAdapter = new BookAdapter(this, getContext());
        recyclerView.setAdapter(bookAdapter);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && bookAdapter!=null && bookAdapter.getItemCount() == 0)
            search();
    }

    private void search() {
        progressBar.setVisibility(View.VISIBLE);
        ApiHelper.getInstance().search("地下城", new ApiHelperCallback<List<Book>>() {
            @Override
            public void onResult(List<Book> result) {
                progressBar.setVisibility(View.GONE);
                Log.d("debuggg", "searched book");
                bookAdapter.setBookList(result);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void selectedBook(String id, final View sharedImageView, final View sharedTextView) {
        progressBar.setVisibility(View.VISIBLE);
        ApiHelper.getInstance().getBook(id, new ApiHelperCallback<Book>() {
            @Override
            public void onResult(Book result) {
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getContext(), BookSummaryActivity.class);
                intent.putExtra("BOOK", result);
                Pair<View, String> p1 = Pair.create(sharedImageView, "cover");
                Pair<View, String> p2 = Pair.create(sharedTextView, "title");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(), p1, p2);

                startActivity(intent, options.toBundle());
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getChapS(String sourceId) {
        ApiHelper.getInstance().getChapterSources(sourceId, new ApiHelperCallback<List<ChapterSource>>() {
            @Override
            public void onResult(List<ChapterSource> result) {
                if (result.get(0).link != null)
                    ApiHelper.getInstance().getChapter(result.get(0).link, new ApiHelperCallback<Chapter>() {
                        @Override
                        public void onResult(Chapter result) {
                            Log.d("debuggg", result.title);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            }

            @Override
            public void onError() {

            }
        });
    }
}
