package com.kyboon.kindlepusher.DataAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyboon.kindlepusher.R;

public class BookshelfAdapter extends RecyclerView.Adapter<BookshelfAdapter.BookshelfViewHolder> {

    public static class BookshelfViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle;
        TextView tvLastChapter;
        TextView tvLastUpdated;
        public BookshelfViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.ivBookCover);
            tvTitle = view.findViewById(R.id.tvBookTitle);
        }
    }

    public BookshelfAdapter() {

    }

    @NonNull
    @Override
    public BookshelfViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book, viewGroup, false);
        return new BookshelfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookshelfViewHolder bookshelfViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
