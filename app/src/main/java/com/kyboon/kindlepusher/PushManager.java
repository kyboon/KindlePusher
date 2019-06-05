package com.kyboon.kindlepusher;

import android.util.Pair;

import com.google.protobuf.Api;
import com.kyboon.kindlepusher.DataTypes.BookSource;
import com.kyboon.kindlepusher.DataTypes.Bookmark;
import com.kyboon.kindlepusher.DataTypes.Chapter;
import com.kyboon.kindlepusher.DataTypes.ChapterSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

interface PushManagerCallback {
    enum ProgressState {
        SyncBookmarks,
        CheckUpdates,
        DownloadUpdates,
        PushUpdates
    }

    void onSuccess();

    void onProgress(float progress, ProgressState state);

    void onError();
}

public class PushManager {

    private static PushManager instance;

    static PushManager getInstance() {
        if (instance == null)
            instance = new PushManager();
        return instance;
    }

    class PushProgress {
        public LinkedHashMap<Bookmark, BookSource> updatedBooks = new LinkedHashMap<>();
        public LinkedHashMap<Pair<Bookmark,String>, String> downloadedContent = new LinkedHashMap<>();
        public int totalBookmarkCount = 0;
        public int checkedBookmarkCount = 0;
        public int downloadedUpdates = 0;
    }

    private static PushProgress pushProgress;

    void pushAll(final PushManagerCallback callback) {
        pushProgress = new PushProgress();
        callback.onProgress(0, PushManagerCallback.ProgressState.SyncBookmarks);
        BookmarkManager.getInstance().syncBookmarks(new BookmarkManagerCallback() {
            @Override
            public void onSuccess() {
                callback.onProgress(1, PushManagerCallback.ProgressState.SyncBookmarks);
                checkAllUpdates(callback);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    private void checkAllUpdates(final PushManagerCallback callback) {
        callback.onProgress(0, PushManagerCallback.ProgressState.CheckUpdates);
        List<Bookmark> allBookmarks = BookmarkManager.getInstance().getBookmarks();
        pushProgress.totalBookmarkCount = allBookmarks.size();
        for (final Bookmark bookmark : allBookmarks) {
            if (bookmark.lastUpdated == null && bookmark.chapterId == null)
                continue;

            ApiHelper.getInstance().getBookSource(bookmark.bookId, new ApiHelperCallback<List<BookSource>>() {
                @Override
                public void onResult(List<BookSource> result) {
                    // if Bookmark doesn't have the update timestamp, means that the last read chapter was not the latest at the time
                    if (bookmark.lastUpdated == null)
                        pushProgress.updatedBooks.put(bookmark, result.get(0));
                    else if (result.size() > 0 && result.get(0).updated.equals(bookmark.lastUpdated)) {
                        pushProgress.updatedBooks.put(bookmark, result.get(0));
                    } else
                        callback.onError();
                    pushProgress.checkedBookmarkCount += 1;
                    callback.onProgress((float) pushProgress.checkedBookmarkCount / (float) pushProgress.totalBookmarkCount, PushManagerCallback.ProgressState.CheckUpdates);
                    if (pushProgress.checkedBookmarkCount == pushProgress.totalBookmarkCount) {
                        DownloadAllUpdates(callback);
                    }
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
        }
    }

    private void DownloadAllUpdates(final PushManagerCallback callback) {
        callback.onProgress(0, PushManagerCallback.ProgressState.DownloadUpdates);
        for (final Map.Entry<Bookmark, BookSource> entry : pushProgress.updatedBooks.entrySet()) {
            ApiHelper.getInstance().getChapterSources(entry.getValue()._id, new ApiHelperCallback<List<ChapterSource>>() {
                @Override
                public void onResult(final List<ChapterSource> result) {
                    List<ChapterSource> newChapterSources = new ArrayList<>();
                    for (ChapterSource chapterSource: result) {
                        if (chapterSource.id == entry.getValue()._id)
                            newChapterSources = result.subList(result.indexOf(chapterSource), result.size() - 1);
                    }
                    CascadeDownloadChapter(newChapterSources, new CascadeCallback() {
                        @Override
                        public void onSuccess(String content) {
                            PushManager.pushProgress.downloadedContent.put(new Pair<>(entry.getKey(), result.get(result.size() - 1).id), content);
                            PushManager.pushProgress.downloadedUpdates += 1;
                            callback.onProgress((float) PushManager.pushProgress.downloadedUpdates / (float) pushProgress.updatedBooks.size(), PushManagerCallback.ProgressState.DownloadUpdates);
                            if (PushManager.pushProgress.downloadedUpdates == pushProgress.updatedBooks.size()) {
                                // push;
                            }
                        }

                        @Override
                        public void onError() {
                            callback.onError();
                        }
                    }, "");
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
        }
    }

    private interface CascadeCallback{
        void onSuccess(String content);
        void onError();
    }

    private void CascadeDownloadChapter(final List<ChapterSource> sources, final CascadeCallback cascadeCallback, final String content) {
        if (sources.size() == 0) {
            cascadeCallback.onSuccess(content);
            return;
        }
        ApiHelper.getInstance().getChapter(sources.get(0).link, new ApiHelperCallback<Chapter>() {
            @Override
            public void onResult(Chapter result) {
                sources.remove(0);
                CascadeDownloadChapter(sources, cascadeCallback, content + "\n\n" + result.title + "\n" +result.content);
            }

            @Override
            public void onError() {
                cascadeCallback.onError();
            }
        });

    }
}
