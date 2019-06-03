package com.kyboon.kindlepusher.DataTypes;

import java.util.List;

public class Bookshelf {
    private List<Bookmark> bookmarks;

    public Bookshelf() {}

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }
}
