package com.kyboon.kindlepusher;

import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.DataTypes.Chapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NovelApi {
    class searchWrapper {
        public List<Book> books;
        public int total;
    }
    @GET("book/fuzzy-search")
    Call<searchWrapper> search(@Query("query") String query);

    @GET("book/548d9c17eb0337ee6df738f5")
    Call<Book> getBooks();

    class ChapterWrapper {
        public boolean ok;
        public Chapter chapter;
    }

    @GET("http://chapterup.zhuishushenqi.com/chapter/{link}")
    Call<ChapterWrapper> getChapterWrapper(@Path("link") String link);

}
