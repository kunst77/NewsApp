package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * A {@Link NewsArticleAdapter} knows how to create a list item layout for each
 * news article in the data source (a list of {@Link NewsArticle} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */

public class NewsArticleAdapter extends ArrayAdapter<NewsArticle> {

    private static final String DATE_SEPARATOR = "T";

    /**
     * Constructs a new {@Link NewsArticleAdapter}
     *
     * @param context      of the app
     * @param newsArticles is the list of news articles, which is the data source of the adapter
     */
    public NewsArticleAdapter(Context context, List<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
    }

    /**
     * Returns a list item view that displays information about each news article at the
     * given position in the list of news articles
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if there is an existing list item view (called convertView) that we can resuse,
        //otherwise, if convertView is null, inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.newsarticle_list_item, parent, false);
        }
        //Find the NewsArticle at the given position in the list of news articles
        NewsArticle currentNewsArticle = getItem(position);

        //Find the TextView with view ID article_title_text
        TextView titleView = (TextView) listItemView.findViewById(R.id.article_title_text);
        titleView.setText(currentNewsArticle.getArticleTitle());

        //Find the TextView with the view ID article_section_text
        TextView sectionText = (TextView) listItemView.findViewById(R.id.article_section_text);
        sectionText.setText(currentNewsArticle.getSectionName());

        //Find the TextView with the view ID article_date_text
        TextView dateText = (TextView) listItemView.findViewById(R.id.article_date_text);

        //retrieve the unformatted date and store it in a variable
        String originalDate = currentNewsArticle.getPublicationDate();

        //separate the parts of the date by the existing T
        String[] dateParts = originalDate.split(DATE_SEPARATOR);

        //store the formatted date in a new string
        String desiredDate = dateParts[0];

        //apply the formatted date to the date TextView
        dateText.setText(desiredDate);

        //Find the TextView with the view ID article_author_text
        TextView authorText = (TextView) listItemView.findViewById(R.id.article_author_text);
        authorText.setText(currentNewsArticle.getAuthor());

        return listItemView;
    }

}
