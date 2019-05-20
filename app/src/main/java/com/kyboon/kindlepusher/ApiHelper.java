package com.kyboon.kindlepusher;

import android.net.Uri;
import android.util.Log;

import com.kyboon.kindlepusher.DataTypes.Book;
import com.squareup.picasso.Picasso;

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
    Retrofit retrofit;
    NovelApi novelApi;

    public static ApiHelper getInstance() {
        if (instance == null)
            return new ApiHelper();
        else
            return instance;
    }

    private ApiHelper() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.zhuishushenqi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        novelApi = retrofit.create(NovelApi.class);
    }

    public void getBook() {
        Call<Book> call = novelApi.getBooks();

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    Log.d("debuggg", response.body().title + response.body().cover);
                    if (response.body().cover != null) {
                        Uri uri =Uri.parse(response.body().cover);
                        Log.d("debuggg", uri.getLastPathSegment());
                        //Picasso.get().load(uri.getLastPathSegment()).into(testIV);
                    }
                } else {
                    Log.d("debuggg", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.d("debuggg", t.getMessage());
            }
        });
    }

    public void getChapter(String link) {
        Call<NovelApi.ChapterWrapper> call = novelApi.getChapterWrapper(link);

        call.enqueue(new Callback<NovelApi.ChapterWrapper>() {
            @Override
            public void onResponse(Call<NovelApi.ChapterWrapper> call, Response<NovelApi.ChapterWrapper> response) {
                if (response.isSuccessful()){

                }
                    //Log.d("debuggg", response.body().chapter.content);
                    //testTV.setText(response.body().chapter.content);
                else {
                    Log.d("debuggg", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<NovelApi.ChapterWrapper> call, Throwable t) {
                Log.d("debuggg", t.getMessage());
            }
        });
    }

    public void search(String query, final ApiHelperCallback callback) {
        Call<NovelApi.searchWrapper> call = novelApi.search(query);

        call.enqueue(new Callback<NovelApi.searchWrapper>() {
            @Override
            public void onResponse(Call<NovelApi.searchWrapper> call, Response<NovelApi.searchWrapper> response) {
                if (response.isSuccessful()) {
//                    String bookNames = "";
//                    for (Book book: response.body().books) {
//                        bookNames += book.title + "\n";
//                    }
//                    //testTV.setText(bookNames);
//                    callback.onResult(bookNames);
//                    Log.d("debuggg", bookNames);
                    callback.onResult(response.body().books);
                } else {
                    Log.d("debuggg", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<NovelApi.searchWrapper> call, Throwable t) {
                Log.d("debuggg", t.getMessage());
            }
        });
    }
}
