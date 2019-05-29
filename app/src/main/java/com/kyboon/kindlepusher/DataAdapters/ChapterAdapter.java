package com.kyboon.kindlepusher.DataAdapters;

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
    }

    List<ChapterSource> chapterList = new ArrayList<>();
    private IChapterAdapter callback;

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        public ChapterViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvChapterTitle);
        }
    }

    public ChapterAdapter(IChapterAdapter callback) {
        this.callback = callback;
    }

    public void setChapterList(List<ChapterSource> chapterList) {
        this.chapterList = chapterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chapter, viewGroup, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder chapterViewHolder, int i) {
        final ChapterSource chapter = chapterList.get(i);
        chapterViewHolder.tvTitle.setText(chapter.title);
        chapterViewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.selectedChapter(chapter.id, chapter.link);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }
}
