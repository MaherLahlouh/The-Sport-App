package com.example.thesportapp.model;

public class PopularLeague {

    private final String id;
    private final String name;
    private final String sport;

    public PopularLeague(String id, String name, String sport) {
        this.id = id;
        this.name = name;
        this.sport = sport;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSport() {
        return sport;
    }
}
