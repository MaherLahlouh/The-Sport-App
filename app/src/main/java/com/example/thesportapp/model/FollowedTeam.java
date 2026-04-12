package com.example.thesportapp.model;

public class FollowedTeam {

    private final String idTeam;
    private final String name;
    private final String badgeUrl;

    public FollowedTeam(String idTeam, String name, String badgeUrl) {
        this.idTeam = idTeam;
        this.name = name;
        this.badgeUrl = badgeUrl;
    }

    public String getIdTeam() {
        return idTeam;
    }

    public String getName() {
        return name;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }
}
