package com.example.thesportapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class TeamsResponse {

    @SerializedName("teams")
    private List<Team> teams;

    public List<Team> getTeams() {
        return teams != null ? teams : Collections.emptyList();
    }
}
