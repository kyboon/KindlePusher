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

    List<Book> bookList = new ArrayList<>();

    public static class BookstoreViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle;
        TextView tvLastChapter;
        TextView tvLastUpdated;
        public BookstoreViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.ivBookCover);
            tvTitle = view.findViewById(R.id.tvBookTitle);
        }
    }

    public BookstoreAdapter() {

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
        Book book = bookList.get(i);
        bookshelfViewHolder.tvTitle.setText(book.title);
        Uri uri = Uri.parse(book.cover);
        Picasso.get().load(uri.getLastPathSegment()).into(bookshelfViewHolder.ivCover);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
