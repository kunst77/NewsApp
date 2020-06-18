package com.example.android.newsapp;

/**
 * An {@Link NewsArticle} object contains information related to a single news article.
 */

public class NewsArticle {

    /**Title of the article*/
    private String mArticleTitle;

    /**News Section of article*/
    private String mSectionName;

    /**Date article was published*/
    private String mPublicationDate;

    /**Author of article*/
    private String mAuthor;

    /**Website URL of news article*/
    private String mUrl;

    /**
     * Constructs a new {@Link NewsArticle} object
     * @param articleTitle is the title of the news article
     * @param sectionName is the section that contains the news article
     * @param publicationDate is the date that the article was published
     * @param author is the author of the article
     * @param url is the URL to be linked to the news article website
     */
    public NewsArticle (String articleTitle, String sectionName, String publicationDate, String author, String url) {
        mArticleTitle = articleTitle;
        mSectionName = sectionName;
        mPublicationDate = publicationDate;
        mAuthor = author;
        mUrl = url;
    }

    //constructor with now author or date for debugging purposes
    public NewsArticle(String articleTitle, String sectionName, String url) {
        mArticleTitle = articleTitle;
        mSectionName = sectionName;
        mUrl = url;
    }

    //returns the article's author
    public String getArticleTitle() {
        return mArticleTitle;
    }

    //returns the section of the article
    public String getSectionName() {
        return mSectionName;
    }

    //returns the publication date
    public String getPublicationDate() {
        return mPublicationDate;
    }

    //returns the name of the author
    public String getAuthor() {
        return mAuthor;
    }

    //returns the URL of the article
    public String getUrl() {
        return mUrl;
    }
}
