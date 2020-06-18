package com.example.android.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements androidx.loader.app.LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    //original web address requested json data.
    private static final String GUARDIAN_API_REQUEST_URL = "https://content.guardianapis.com/search?api-key=test";

    //constant value for newsarticle loader ID.  Only comes into play if using multiple loaders
    private static final int NEWSARTICLE_LOADER_ID = 1;

    private int newsArticleLoaderId = 1;

    //Adapter for the list of news articles
    private NewsArticleAdapter mAdapter;

    //TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    //string to hold search term
    private String searchTerm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find a reference to the {@Link ListView} in the layout
//        ListView newsArticleListView = (ListView) findViewById(R.id.list);
        ListView newsArticleListView = (ListView) findViewById(R.id.list);

        //point the activity to the empty text view when there are no news articles returned
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsArticleListView.setEmptyView(mEmptyStateTextView);

        //Create a new adapter that takes an empty list of news articles as input
        mAdapter = new NewsArticleAdapter(this, new ArrayList<NewsArticle>());

        //Set the adapter on the {@Link ListView}
        //so the list can be populated in the user interface
        newsArticleListView.setAdapter(mAdapter);

        //Set an item click listener on the ListView, which sends an intent to a web browser
        //to open a website with more information about the selected news article.
        newsArticleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Find the current news article that was clicked on
                NewsArticle currentArticle = mAdapter.getItem(position);

                //Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsArticleUri = Uri.parse(currentArticle.getUrl());

                //Create a new intent to view the news article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsArticleUri);

                //Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get the details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            //Get a reference to the LoaderManager, in order to interact with Loaders.
            //Initialize the loader.  Pass in the int ID constant defined above and pass in null for
            //the bundle.  Pass in this activity for the LoaderCallbacks parameter (which is valid
            //because this activity implements the LoaderCallbacks interface).
            LoaderManager.getInstance(this).initLoader(NEWSARTICLE_LOADER_ID, null, this);

        } else {
            //Otherwise, display error
            //First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            //Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        Button searchButton = (Button) findViewById(R.id.search_button);
        final EditText searchField = (EditText) findViewById(R.id.search_field);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Let the use know they didn't search a term
                String providedSearch = searchField.getText().toString();
                if (providedSearch.matches("")) {
                    Toast.makeText(getApplicationContext(), "You haven't entered a search term", Toast.LENGTH_SHORT).show();
                    return;
                }
                //update the search term and look for news articles with term
                searchTerm = providedSearch;
                //Clear out the current adapter
                mAdapter.clear();
                //restart the loadermanager with the new search term
                getSupportLoaderManager().restartLoader(NEWSARTICLE_LOADER_ID, null, MainActivity.this);

            }

        });
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int id, Bundle bundle) {

        //parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_API_REQUEST_URL);
        Log.v("urlrequested", "urlrequested is: " + GUARDIAN_API_REQUEST_URL);

        //buildUpon prepares the baseUri that we just parsed so we can add parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //Append query parameter and its value.
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("show-tags", "contributor");

        //the second parameter is sent to rebuild the loader with the user's custom term
        uriBuilder.appendQueryParameter("q", searchTerm);

        //return the completed uri
        return new NewsArticleLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticles) {
        //Hide loading indicator because the data has been loaded
        View loadingIndicator =findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        //Set the empty text state to display "No news articles found"
        mEmptyStateTextView.setText(R.string.no_articles);

        //Clear the adapter of previous news articles
        mAdapter.clear();

        //If there is a valid set of {@Link NewsArticle}s, add them to the adapter's
        //data set.  This will trigger the ListView to update
        if (newsArticles != null && !newsArticles.isEmpty()) {
            mAdapter.addAll(newsArticles);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        //Loader reset, so we can clear the existing data
        mAdapter.clear();

    }

}