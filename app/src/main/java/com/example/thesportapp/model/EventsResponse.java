package com.example.thesportapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class EventsResponse {

    @SerializedName("events")
    private List<MatchEvent> events;

    public List<MatchEvent> getEvents() {
        return events != null ? events : Collections.emptyList();
    }
}
