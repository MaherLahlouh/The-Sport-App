package com.example.thesportapp.model;

import com.google.gson.annotations.SerializedName;

public class Team {

    @SerializedName("idTeam")
    private String idTeam;

    @SerializedName("strTeam")
    private String strTeam;

    @SerializedName("strTeamBadge")
    private String strTeamBadge;

    @SerializedName("strSport")
    private String strSport;

    public String getIdTeam() {
        return idTeam;
    }

    public String getStrTeam() {
        return strTeam != null ? strTeam : "";
    }

    public String getStrTeamBadge() {
        return strTeamBadge;
    }

    public String getStrSport() {
        return strSport;
    }
}
