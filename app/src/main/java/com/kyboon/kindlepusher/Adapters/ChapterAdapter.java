package com.kyboon.kindlepusher.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kyboon.kindlepusher.DataTypes.ChapterSource;
import com.kyboon.kindlepusher.R;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    public interface IChapterAdapter {
        void selectedChapter(String id, String link);
        void longPressedChapter(String id, String link);
    }

    List<ChapterSource> chapterList = new ArrayList<>();
    private IChapterAdapter callback;
    private boolean ascending = true;

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        String id;
        String link;
        public ChapterViewHolder(View view, final IChapterAdapter callback) {
            super(view);
            tvTitle = view.findViewById(R.id.tvChapterTitle);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.selectedChapter(id, link);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    callback.longPressedChapter(id, link);
                    return true;
                }
            });
        }
    }

    public ChapterAdapter(IChapterAdapter callback) {
        this.callback = callback;
    }

    public void setChapterList(List<ChapterSource> chapterList) {
        this.chapterList = chapterList;
        notifyDataSetChanged();
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chapter, viewGroup, false);
        return new ChapterViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder chapterViewHolder, int i) {
        final ChapterSource chapter = chapterList.get(ascending ? i : chapterList.size() - i -1);
        chapterViewHolder.tvTitle.setText(chapter.title);
        chapterViewHolder.id = chapter.id;
        chapterViewHolder.link = chapter.link;
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }
}
