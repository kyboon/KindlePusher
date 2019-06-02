package com.kyboon.kindlepusher.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    public enum AdapterType {
        Search,
        Ranking,
        Shelf
    }
    public interface IBookAdapter {
        void selectedBook(String id, View sharedImageView, View sharedTextView);
    }

    List<Book> bookList = new ArrayList<>();
    private IBookAdapter callback;
    private Context context;
    private AdapterType adapterType = AdapterType.Shelf;

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivCover;
        TextView tvTitle;
        TextView tvSecondary;
        TextView tvTertiary;
        public BookViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card);
            ivCover = view.findViewById(R.id.ivBookCover);
            tvTitle = view.findViewById(R.id.tvBookTitle);
            tvSecondary = view.findViewById(R.id.tvSecondary);
            tvTertiary = view.findViewById(R.id.tvTertiary);
        }
    }

    public BookAdapter(IBookAdapter callback, Context context) {
        this.callback = callback;
        this.context = context;
    }

    public void setBookList(List<Book> bookList, AdapterType adapterType) {
        this.bookList = bookList;
        this.adapterType = adapterType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book, viewGroup, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookViewHolder bookViewHolder, int i) {
        final Book book = bookList.get(i);
        bookViewHolder.tvTitle.setText(book.title);
        switch (adapterType) {
            case Search:
                bookViewHolder.tvSecondary.setText(book.cat + " | " + book.author);
                bookViewHolder.tvTertiary.setText(book.shortIntro);
                break;
            case Shelf:
                break;
            case Ranking:
                bookViewHolder.tvSecondary.setText(book.minorCate + " | " + book.author);
                bookViewHolder.tvTertiary.setText(book.shortIntro);
                break;
        }

        Uri uri = Uri.parse(book.cover);
        Picasso.get().load(uri.getLastPathSegment()).resize(200, 200).into(bookViewHolder.ivCover, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) bookViewHolder.ivCover.getDrawable()).getBitmap();
                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                imageDrawable.setCircular(true);
                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                bookViewHolder.ivCover.setImageDrawable(imageDrawable);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        bookViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.selectedBook(book.id, bookViewHolder.ivCover, bookViewHolder.tvTitle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
