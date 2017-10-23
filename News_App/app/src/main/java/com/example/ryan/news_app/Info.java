package com.example.ryan.news_app;

/**
 * Created by Ryan on 10/21/2017.
 */

public class Info {

    String title;
    String author;
    String url;
    String date;
    String section;

    public Info(String title, String author, String url, String date, String section) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.date = date;
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "Info{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                ", date='" + date + '\'' +
                ", section='" + section + '\'' +
                '}';
    }
}
