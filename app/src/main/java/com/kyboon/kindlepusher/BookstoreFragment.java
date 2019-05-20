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

import com.kyboon.kindlepusher.DataAdapters.BookshelfAdapter;
import com.kyboon.kindlepusher.DataAdapters.BookstoreAdapter;
import com.kyboon.kindlepusher.DataTypes.Book;

import java.util.List;

public class BookstoreFragment extends Fragment {
    BookstoreAdapter bookstoreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bookstore, container, false);


        RecyclerView recyclerView = rootView.findViewById(R.id.rvBookstore);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookstoreAdapter = new BookstoreAdapter();
        recyclerView.setAdapter(bookstoreAdapter);

        search();

        return rootView;
    }

    private void search() {
        ApiHelper.getInstance().search("地下城", new ApiHelperCallback<List<Book>>() {
            @Override
            public void onResult(List<Book> result) {
                Log.d("debuggg", "searched book");
                bookstoreAdapter.setBookList(result);
            }

            @Override
            public void onError() {

            }
        });
    }
}
