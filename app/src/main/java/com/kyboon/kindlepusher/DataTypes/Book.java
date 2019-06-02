package com.kyboon.kindlepusher.DataTypes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Book implements Parcelable {

    class BookRating {
        public int count;
        public float score;
    }

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
    public String cat;
    public String majorCate;
    public String majorCateV2;
    public String minorCate;
    public String minorCateV2;

    protected Book(Parcel in) {
        id = in.readString();
        shortIntro = in.readString();
        longIntro = in.readString();
        author = in.readString();
        title = in.readString();
        updated = in.readString();
        lastChapter = in.readString();
        cover = in.readString();
        chaptersCount = in.readInt();
        byte tmpIsSerial = in.readByte();
        isSerial = tmpIsSerial == 0 ? null : tmpIsSerial == 1;
        wordCount = in.readInt();
        cat = in.readString();
        majorCate = in.readString();
        majorCateV2 = in.readString();
        minorCate = in.readString();
        minorCateV2 = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(shortIntro);
        dest.writeString(longIntro);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(updated);
        dest.writeString(lastChapter);
        dest.writeString(cover);
        dest.writeInt(chaptersCount);
        dest.writeByte((byte) (isSerial == null ? 0 : isSerial ? 1 : 2));
        dest.writeInt(wordCount);
        dest.writeString(cat);
        dest.writeString(majorCate);
        dest.writeString(majorCateV2);
        dest.writeString(minorCate);
        dest.writeString(minorCateV2);
    }

    public String getLongIntro() {
        String[] paragraphs = longIntro.split("\\s+\\s+");
        String formattedIntro = "";
        for(String paragraph: paragraphs) {
            formattedIntro += "      " + paragraph + "\n";
        }
        return formattedIntro;
    }
}
