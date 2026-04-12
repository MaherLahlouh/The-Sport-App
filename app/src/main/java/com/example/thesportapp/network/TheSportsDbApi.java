package com.example.thesportapp.network;

import com.example.thesportapp.model.EventsResponse;
import com.example.thesportapp.model.TeamsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheSportsDbApi {

    @GET("1/json/123/eventsday.php")
    Call<EventsResponse> getEventsForDay(@Query("d") String dateIso, @Query("s") String sport);

    @GET("1/json/123/eventsnextleague.php")
    Call<EventsResponse> getNextEventsForLeague(@Query("id") String leagueId);

    @GET("1/json/123/eventspastleague.php")
    Call<EventsResponse> getPastEventsForLeague(@Query("id") String leagueId);

    @GET("1/json/123/eventsnext.php")
    Call<EventsResponse> getNextEventsForTeam(@Query("id") String teamId);

    @GET("1/json/123/searchteams.php")
    Call<TeamsResponse> searchTeams(@Query("t") String query);
}
