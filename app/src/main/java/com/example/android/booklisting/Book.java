package com.example.android.booklisting;

/**
 * Created by gurkaran on 12-02-2017.
 */

public class Book {
    private String mtitle;
    private String mauthor;
    private String murl;
    public Book(String title, String author, String url)
    {   mtitle=title;
        mauthor=author;
        murl=url;
    }

    public String getTitle() {
        return mtitle;
    }

    public String getAuthor() {
        return mauthor;
    }

    public String getUrl() {    return murl; }
}
