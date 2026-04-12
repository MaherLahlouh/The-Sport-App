package com.example.thesportapp.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import com.example.thesportapp.network.FlexibleStringTypeAdapter;

public class MatchEvent {

    @SerializedName("idEvent")
    private String idEvent;

    @SerializedName("strTimestamp")
    private String strTimestamp;

    @SerializedName("strHomeTeam")
    private String strHomeTeam;

    @SerializedName("strAwayTeam")
    private String strAwayTeam;

    @JsonAdapter(FlexibleStringTypeAdapter.class)
    @SerializedName("intHomeScore")
    private String intHomeScore;

    @JsonAdapter(FlexibleStringTypeAdapter.class)
    @SerializedName("intAwayScore")
    private String intAwayScore;

    @SerializedName("strStatus")
    private String strStatus;

    @SerializedName("strLeague")
    private String strLeague;

    @SerializedName("strLeagueBadge")
    private String strLeagueBadge;

    @SerializedName("strHomeTeamBadge")
    private String strHomeTeamBadge;

    @SerializedName("strAwayTeamBadge")
    private String strAwayTeamBadge;

    @SerializedName("dateEvent")
    private String dateEvent;

    @SerializedName("strTime")
    private String strTime;

    @SerializedName("strTimeLocal")
    private String strTimeLocal;

    @SerializedName("strVenue")
    private String strVenue;

    @SerializedName("strSport")
    private String strSport;

    public String getIdEvent() {
        return idEvent;
    }

    public String getStrTimestamp() {
        return strTimestamp;
    }

    public String getStrHomeTeam() {
        return strHomeTeam != null ? strHomeTeam : "";
    }

    public String getStrAwayTeam() {
        return strAwayTeam != null ? strAwayTeam : "";
    }

    public String getIntHomeScore() {
        return intHomeScore;
    }

    public String getIntAwayScore() {
        return intAwayScore;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public String getStrLeague() {
        return strLeague != null ? strLeague : "";
    }

    public String getStrLeagueBadge() {
        return strLeagueBadge;
    }

    public String getStrHomeTeamBadge() {
        return strHomeTeamBadge;
    }

    public String getStrAwayTeamBadge() {
        return strAwayTeamBadge;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public String getStrTime() {
        return strTime;
    }

    public String getStrTimeLocal() {
        return strTimeLocal;
    }

    public String getStrVenue() {
        return strVenue;
    }

    public String getStrSport() {
        return strSport;
    }

    public String getScoreLine() {
        String hs = intHomeScore != null && !intHomeScore.isEmpty() ? intHomeScore : "–";
        String as = intAwayScore != null && !intAwayScore.isEmpty() ? intAwayScore : "–";
        return hs + " : " + as;
    }

    public boolean hasScore() {
        return intHomeScore != null && !intHomeScore.isEmpty()
                && intAwayScore != null && !intAwayScore.isEmpty();
    }
}
