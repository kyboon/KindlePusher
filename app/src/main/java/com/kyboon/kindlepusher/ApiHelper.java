package com.kyboon.kindlepusher;

import android.util.Log;

import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.DataTypes.BookSource;
import com.kyboon.kindlepusher.DataTypes.Chapter;
import com.kyboon.kindlepusher.DataTypes.ChapterSource;

import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

interface ApiHelperCallback<T> {
    void onResult(T result);

    void onError();
}

public class ApiHelper {

    private static ApiHelper instance;
    private NovelApi novelApi;

    static ApiHelper getInstance() {
        if (instance == null)
            instance = new ApiHelper();
        return instance;
    }

    private ApiHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.zhuishushenqi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        novelApi = retrofit.create(NovelApi.class);
    }

    void getBookSource(String id, final ApiHelperCallback<List<BookSource>> callback) {
        Call<List<BookSource>> call = novelApi.getBookSource("summary", id);

        Log.v("ApiHelper", "Getting Book Source with id: " + id);
        call.enqueue(new Callback<List<BookSource>>() {
            @Override
            public void onResponse(Call<List<BookSource>> call, Response<List<BookSource>> response) {
                if (response.isSuccessful()) {
                    callback.onResult(response.body());
                } else {
                    Log.w("ApiHelper", "getBookSource() Unsuccessful Response, Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BookSource>> call, Throwable t) {
                Log.e("ApiHelper", "getBookSource() Error, Message: " + t.getMessage());
            }
        });
    }

    void getBook(String id, final ApiHelperCallback<Book> callback) {
        Call<Book> call = novelApi.getBooks(id);

        Log.v("ApiHelper", "Getting Book with id: " + id);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    callback.onResult(response.body());
                } else {
                    Log.w("ApiHelper", "getBook() Unsuccessful Response, Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("ApiHelper", "getBook() Error, Message: " + t.getMessage());
            }
        });
    }

    void getChapter(String link, final ApiHelperCallback<Chapter> callback) {
        Call<NovelApi.ChapterWrapper> call = novelApi.getChapterWrapper(link);

        Log.v("ApiHelper", "Getting Chapter with link: " + link);
        call.enqueue(new Callback<NovelApi.ChapterWrapper>() {
            @Override
            public void onResponse(Call<NovelApi.ChapterWrapper> call, Response<NovelApi.ChapterWrapper> response) {
                if (response.isSuccessful() && response.body().ok) {
                    callback.onResult(response.body().chapter);
                } else {
                    Log.w("ApiHelper", "getChapter() Unsuccessful Response, Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<NovelApi.ChapterWrapper> call, Throwable t) {
                Log.e("ApiHelper", "getChapter() Error, Message: " + t.getMessage());
            }
        });
    }

    void getChapterSources(String bookSourceId, final ApiHelperCallback<List<ChapterSource>> callback) {
        Call<NovelApi.ChapterSourceWrapper> call = novelApi.getChapterSource(bookSourceId);

        Log.v("ApiHelper", "Getting Chapter Sources with Book Source Id: " + bookSourceId);
        call.enqueue(new Callback<NovelApi.ChapterSourceWrapper>() {
            @Override
            public void onResponse(Call<NovelApi.ChapterSourceWrapper> call, Response<NovelApi.ChapterSourceWrapper> response) {
                if (response.isSuccessful()) {
                    response.body().chapters.sort(new Comparator<ChapterSource>() {
                        @Override
                        public int compare(ChapterSource o1, ChapterSource o2) {
                            return o1.order - o2.order;
                        }
                    });
                    callback.onResult(response.body().chapters);
                } else {
                    Log.w("ApiHelper", "getChapterSources() Unsuccessful Response, Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<NovelApi.ChapterSourceWrapper> call, Throwable t) {
                Log.e("ApiHelper", "getChapterSources() Error, Message: " + t.getMessage());
            }
        });
    }

    void search(String query, final ApiHelperCallback<List<Book>> callback) {
        Call<NovelApi.SearchWrapper> call = novelApi.search(query);

        Log.v("ApiHelper", "Getting search result for: " + query);
        call.enqueue(new Callback<NovelApi.SearchWrapper>() {
            @Override
            public void onResponse(Call<NovelApi.SearchWrapper> call, Response<NovelApi.SearchWrapper> response) {
                if (response.isSuccessful()) {
                    callback.onResult(response.body().books);
                } else
                    Log.w("ApiHelper", "search() Unsuccessful Response, Code: " + response.code());
            }

            @Override
            public void onFailure(Call<NovelApi.SearchWrapper> call, Throwable t) {
                Log.e("ApiHelper", "search() Error, Message: " + t.getMessage());
            }
        });
    }

    void getRankings(final ApiHelperCallback<NovelApi.RankingsWrapper> callback) {
        Call<NovelApi.RankingsWrapper> call = novelApi.getRankings();

        Log.v("ApiHelper", "Getting all rankings");
        call.enqueue(new Callback<NovelApi.RankingsWrapper>() {
            @Override
            public void onResponse(Call<NovelApi.RankingsWrapper> call, Response<NovelApi.RankingsWrapper> response) {
                if (response.isSuccessful()) {
                    callback.onResult(response.body());
                } else
                    Log.w("ApiHelper", "getRankings() Unsuccessful Response, Code: " + response.code());
            }

            @Override
            public void onFailure(Call<NovelApi.RankingsWrapper> call, Throwable t) {
                Log.e("ApiHelper", "getRankings() Error, Message: " + t.getMessage());
            }
        });
    }

    void getRankingResult(String id, final ApiHelperCallback<List<Book>> callback) {
        Call<NovelApi.RankingResultWrapper> call = novelApi.getRankingResults(id);

        Log.v("ApiHelper", "Getting ranking results for: " + id);
        call.enqueue(new Callback<NovelApi.RankingResultWrapper>() {
            @Override
            public void onResponse(Call<NovelApi.RankingResultWrapper> call, Response<NovelApi.RankingResultWrapper> response) {
                if (response.isSuccessful()) {
                    callback.onResult(response.body().ranking.books);
                } else
                    Log.w("ApiHelper", "getRankingResult() Unsuccessful Response, Code: " + response.code());
            }

            @Override
            public void onFailure(Call<NovelApi.RankingResultWrapper> call, Throwable t) {
                Log.e("ApiHelper", "getRankingResult() Error, Message: " + t.getMessage());
            }
        });
    }
}
