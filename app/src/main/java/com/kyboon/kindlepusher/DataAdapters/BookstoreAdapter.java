package com.kyboon.kindlepusher.DataAdapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookstoreAdapter extends RecyclerView.Adapter<BookstoreAdapter.BookstoreViewHolder> {
    public interface IBookstoreAdapter {
        void selectedBook(String id);
    }

    List<Book> bookList = new ArrayList<>();
    private IBookstoreAdapter callback;

    public static class BookstoreViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle;
        TextView tvLastChapter;
        TextView tvLastUpdated;
        public BookstoreViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.ivBookCover);
            tvTitle = view.findViewById(R.id.tvBookTitle);
            tvLastChapter = view.findViewById(R.id.tvLatestChapter);
            tvLastUpdated = view.findViewById(R.id.tvLastUpdated);
        }
    }

    public BookstoreAdapter(IBookstoreAdapter callback) {
        this.callback = callback;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookstoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book, viewGroup, false);
        return new BookstoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookstoreViewHolder bookshelfViewHolder, int i) {
        final Book book = bookList.get(i);
        bookshelfViewHolder.tvTitle.setText(book.title);
        bookshelfViewHolder.tvLastChapter.setText(book.lastChapter);
        //bookshelfViewHolder.tvLastUpdated.setText(book.updated.toString());
        Uri uri = Uri.parse(book.cover);
        Picasso.get().load(uri.getLastPathSegment()).into(bookshelfViewHolder.ivCover);
        bookshelfViewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.selectedBook(book.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
