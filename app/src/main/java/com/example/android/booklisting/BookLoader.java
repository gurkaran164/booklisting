package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by gurkaran on 12-02-2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Book> books = Utils.fetchBookData(mUrl);
        return books;
    }
}
