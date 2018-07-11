package com.christopherbare.inclass07;

import java.net.URL;

public class Article {
    String authorName, releaseDate, title, imageURL, articleURL, description;

    public Article() {
    }

    public Article(String imageURL, String articleURL, String authorName, String releaseDate, String title) {
        this.imageURL = imageURL;
        this.articleURL = articleURL;
        this.authorName = authorName;
        this.releaseDate = releaseDate;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getArticleURL() {
        return articleURL;
    }

    public void setArticleURL(String articleURL) {
        this.articleURL = articleURL;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Article{" +
                "imageURL=" + imageURL +
                ", articleURL=" + articleURL +
                ", authorName='" + authorName + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
