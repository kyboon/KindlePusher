package com.kyboon.kindlepusher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.kyboon.kindlepusher.Adapters.BookmarkAdapter;
import com.kyboon.kindlepusher.ApiHelper;
import com.kyboon.kindlepusher.ApiHelperCallback;
import com.kyboon.kindlepusher.BookSummaryActivity;
import com.kyboon.kindlepusher.BookmarkManager;
import com.kyboon.kindlepusher.BookmarkManagerCallback;
import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.DataTypes.Bookmark;
import com.kyboon.kindlepusher.R;

import java.util.List;

public class ShelfFragment extends Fragment implements BookmarkAdapter.IBookmarkAdapter {

    BookmarkAdapter bookmarkAdapter;
    RecyclerView recyclerView;

    SwipeRefreshLayout srlBook;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shelf, container, false);
        recyclerView = rootView.findViewById(R.id.rvBookmark);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarkAdapter = new BookmarkAdapter(this, getContext());
        recyclerView.setAdapter(bookmarkAdapter);

        progressBar = rootView.findViewById(R.id.progress_circular);

        srlBook = rootView.findViewById(R.id.srlBookmark);
        srlBook.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBookmarks();
            }
        });
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && bookmarkAdapter!=null && bookmarkAdapter.getItemCount() == 0)
            getBookmarks();
    }

    private void getBookmarks() {
        srlBook.setRefreshing(true);
        BookmarkManager.getInstance().syncBookmarks(new BookmarkManagerCallback() {
            @Override
            public void onSuccess() {
                srlBook.setRefreshing(false);
                List<Bookmark> bookmarks = BookmarkManager.getInstance().getBookmarks();
                bookmarkAdapter.setBookmarkList(bookmarks);
            }

            @Override
            public void onError() {
                srlBook.setRefreshing(false);
            }
        });
    }

    @Override
    public void selectedBookmark(String id, final View sharedImageView, final View sharedTextView) {
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
}
