package com.kyboon.kindlepusher.DataTypes;

public class Bookmark {
    public String bookTitle;
    public String bookId;
    public String cover;
    public String chapterTitle;
    public String chapterId;
    public Bookmark() {}
    public Bookmark(String bookTitle, String bookId, String cover) {
        this.bookTitle = bookTitle;
        this.bookId = bookId;
        this.cover = cover;
    }

    public Bookmark setChapter(String chapterTitle, String chapterId) {
        this.chapterId = chapterId;
        this.chapterTitle = chapterTitle;
        return this;
    }
}
