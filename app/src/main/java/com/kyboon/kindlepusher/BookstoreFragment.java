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

import com.kyboon.kindlepusher.DataAdapters.BookstoreAdapter;
import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.DataTypes.BookSource;
import com.kyboon.kindlepusher.DataTypes.Chapter;
import com.kyboon.kindlepusher.DataTypes.ChapterSource;

import java.util.List;

public class BookstoreFragment extends Fragment implements BookstoreAdapter.IBookstoreAdapter {
    BookstoreAdapter bookstoreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bookstore, container, false);


        RecyclerView recyclerView = rootView.findViewById(R.id.rvBookstore);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookstoreAdapter = new BookstoreAdapter(this);
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

    @Override
    public void selectedBook(String id) {
        ApiHelper.getInstance().getBook(id, new ApiHelperCallback<Book>() {
            @Override
            public void onResult(Book result) {
                Log.d("debuggg", result.title);
            }

            @Override
            public void onError() {

            }
        });
        ApiHelper.getInstance().getBookSource(id, new ApiHelperCallback<List<BookSource>>() {
            @Override
            public void onResult(List<BookSource> result) {
                for (BookSource bs: result) {
                    if (bs.starting == true) {
                        getChapS(bs._id);
                        break;
                    }
                }
//                String link = null;
//                try {
//                    link = URLEncoder.encode(result.get(0).link, StandardCharsets.UTF_8.toString());
//                    Log.d("debuggg", link);
//                    ApiHelper.getInstance().getChapter(link);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                ApiHelper.getInstance().getChapter("http://vip.zhuishushenqi.com/chapter/5817f1161bb2ca566b0a5973?cv=1481275033588");
            }

            @Override
            public void onError() {

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
