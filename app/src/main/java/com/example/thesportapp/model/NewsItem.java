package com.example.thesportapp.model;

public class NewsItem {

    private final String title;
    private final String link;

    public NewsItem(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
