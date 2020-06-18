package com.example.android.newsapp;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsArticleLoader extends AsyncTaskLoader<List<NewsArticle>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsArticleLoader.class.getName();

    private String mURL;

    /**
     * Constructs a new {@Link NewsArticleLoader}
     *
     * @param context of the activity
     * @param url     to load the data from
     */
    public NewsArticleLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsArticle> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        //Perform the network request, parse the response, and extract a list of news articles
        List<NewsArticle> newsArticles = QueryUtils.fetchNewsArticleData(mURL);
        return newsArticles;
    }

}
