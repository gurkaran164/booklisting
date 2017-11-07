package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private BookAdapter mAdapter;
    private ProgressBar mprogress;
    private TextView mEmptyStateTextView;
    private static final int BOOK_LOADER_ID = 1;
    private  String USGS_REQUEST_URL ="https://www.googleapis.com/books/v1/volumes?maxResults=10&q=";
    String uriText;
    Button searchButton;
    ListView bookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
         bookListView = (ListView) findViewById(R.id.list);
        mprogress = (ProgressBar) findViewById(R.id.progress);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);
        EditText searchQuery = (EditText) findViewById(R.id.search_query);
        uriText = searchQuery.getText().toString();
        searchButton = (Button) findViewById(R.id.search);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book currentBook = mAdapter.getItem(position);

                Uri bookUri = Uri.parse(currentBook.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                startActivity(websiteIntent);
            }
        });
        mprogress.setVisibility(GONE);
        mEmptyStateTextView.setText(R.string.search);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEmptyStateTextView.setVisibility(View.INVISIBLE);
                    if (uriText != null) {
                        bookListView.setAdapter(mAdapter);
                        LoaderManager loaderManager = getLoaderManager();
                        loaderManager.initLoader(BOOK_LOADER_ID, null,MainActivity.this);
                    } else {
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            mEmptyStateTextView.setText(R.string.internet);
            mprogress.setVisibility(GONE);
        }
    }
    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        EditText searchQuery = (EditText) findViewById(R.id.search_query);
        String uriText = searchQuery.getText().toString();
        USGS_REQUEST_URL += uriText;
        return new BookLoader(this, USGS_REQUEST_URL);
    }
    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        View progress = findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);
        mEmptyStateTextView.setText("No books");
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
