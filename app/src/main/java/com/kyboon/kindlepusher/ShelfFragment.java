package com.kyboon.kindlepusher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyboon.kindlepusher.Adapters.BookmarkAdapter;
import com.kyboon.kindlepusher.DataTypes.Bookmark;

import java.util.List;

public class ShelfFragment extends Fragment implements BookmarkAdapter.IBookmarkAdapter {

    BookmarkAdapter bookmarkAdapter;
    RecyclerView recyclerView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shelf, container, false);
        recyclerView = rootView.findViewById(R.id.rvBookmark);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarkAdapter = new BookmarkAdapter(this, getContext());
        recyclerView.setAdapter(bookmarkAdapter);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && bookmarkAdapter!=null && bookmarkAdapter.getItemCount() == 0)
            getBookmarks();
    }

    private void getBookmarks() {
        BookmarkManager.getInstance().syncBookmarks(new BookmarkManagerCallback() {
            @Override
            public void onSuccess() {
                List<Bookmark> bookmarks = BookmarkManager.getInstance().getBookmarks();
                bookmarkAdapter.setBookmarkList(bookmarks);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void selectedBook(String id, View sharedImageView, View sharedTextView) {

    }
}
