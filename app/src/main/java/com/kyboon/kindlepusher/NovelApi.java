package com.kyboon.kindlepusher;

import com.google.gson.annotations.SerializedName;
import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.DataTypes.Chapter;
import com.kyboon.kindlepusher.DataTypes.BookSource;
import com.kyboon.kindlepusher.DataTypes.ChapterSource;
import com.kyboon.kindlepusher.DataTypes.Ranking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NovelApi {

    // Search
    class SearchWrapper {
        public List<Book> books;
        public int total;
    }

    @GET("book/fuzzy-search")
    Call<SearchWrapper> search(@Query("query") String query);

    // Book
    @GET("book/{id}")
    Call<Book> getBooks(@Path("id") String bookId);

    // Book Source
    @GET("atoc")
    Call<List<BookSource>> getBookSource(@Query("view") String view, @Query("book") String bookId);

    // Chapter Source
    class ChapterSourceWrapper {
        @SerializedName("id")
        public String _id;
        public String name;
        @SerializedName("book")
        public String bookId;
        public String updated;
        public List<ChapterSource> chapters;
    }

    @GET("atoc/{sourceId}?view=chapters")
    Call<ChapterSourceWrapper> getChapterSource(@Path("sourceId") String sourceId);

    // Chapter
    class ChapterWrapper {
        public boolean ok;
        public Chapter chapter;
    }

    @GET("http://chapterup.zhuishushenqi.com/chapter/{link}")
    Call<ChapterWrapper> getChapterWrapper(@Path("link") String link);

    // Ranking
    class GeneralRankingWrapper {
        public boolean ok;
        public List<Ranking> rankings;
    }

    @GET("ranking")
    Call<GeneralRankingWrapper> getGeneralRanking();


}
