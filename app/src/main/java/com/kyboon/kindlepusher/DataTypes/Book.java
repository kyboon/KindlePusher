package com.kyboon.kindlepusher.DataTypes;

import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName("_id")
    public String id;
    public String shortIntro;
    public String longIntro;
    public String author;
    public String title;
    public String updated;
    public String lastChapter;
    public String cover;
    public int chaptersCount;
    public Boolean isSerial;
    public int wordCount;
    public String majorCate;
    public String majorCateV2;
    public String minorCate;
    public String minorCateV2;

    class BookRating {
        public int count;
        public float score;
    }
}
