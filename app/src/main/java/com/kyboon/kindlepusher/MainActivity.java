package com.kyboon.kindlepusher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView testIV;
    TextView testTV;
    NovelApi novelApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testIV = findViewById(R.id.testIV);
        testTV = findViewById(R.id.testTV);



        //getBook();
        //getChapter("http://www.snwx.com/book/15/15231/4307839.html");
        ApiHelper.getInstance().search("地下城", new ApiHelperCallback<String>() {
            @Override
            public void onResult(String result) {
                testTV.setText(result);
            }

            @Override
            public void onError() {

            }
        });
    }


}
