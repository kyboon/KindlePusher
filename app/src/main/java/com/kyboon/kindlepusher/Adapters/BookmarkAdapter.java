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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyboon.kindlepusher.DataTypes.Bookmark;
import com.kyboon.kindlepusher.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {
    public interface IBookmarkAdapter {
        void selectedBook(String id, View sharedImageView, View sharedTextView);
    }

    List<Bookmark> bookmarks = new ArrayList<>();
    private IBookmarkAdapter callback;
    private Context context;

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivCover;
        TextView tvTitle;
        TextView tvSecondary;
        TextView tvTertiary;
        public BookmarkViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card);
            ivCover = view.findViewById(R.id.ivBookCover);
            tvTitle = view.findViewById(R.id.tvBookTitle);
            tvSecondary = view.findViewById(R.id.tvSecondary);
            tvTertiary = view.findViewById(R.id.tvTertiary);
        }
    }

    public BookmarkAdapter(IBookmarkAdapter callback, Context context) {
        this.callback = callback;
        this.context = context;
    }

    public void setBookmarkList(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bookmark, viewGroup, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkViewHolder bookmarkViewHolder, int i) {
        final Bookmark bookmark = bookmarks.get(i);
        bookmarkViewHolder.tvTitle.setText(bookmark.bookTitle);

        if (bookmark.cover != null) {
            Uri uri = Uri.parse(bookmark.cover);
            Picasso.get().load(uri.getLastPathSegment()).resize(200, 200).into(bookmarkViewHolder.ivCover, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap imageBitmap = ((BitmapDrawable) bookmarkViewHolder.ivCover.getDrawable()).getBitmap();
                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                    imageDrawable.setCircular(true);
                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                    bookmarkViewHolder.ivCover.setImageDrawable(imageDrawable);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        bookmarkViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.selectedBook(bookmark.bookId, bookmarkViewHolder.ivCover, bookmarkViewHolder.tvTitle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }
}
