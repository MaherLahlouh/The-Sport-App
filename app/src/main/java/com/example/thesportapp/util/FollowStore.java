package com.example.thesportapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.thesportapp.model.FollowedTeam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FollowStore {

    private static final String PREFS = "follow_store";
    private static final String KEY_TEAMS = "teams_json";

    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    public FollowStore(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public List<FollowedTeam> getFollowedTeams() {
        String json = prefs.getString(KEY_TEAMS, "[]");
        Type type = new TypeToken<List<FollowedTeamJson>>() {
        }.getType();
        List<FollowedTeamJson> raw = gson.fromJson(json, type);
        if (raw == null) {
            return new ArrayList<>();
        }
        List<FollowedTeam> out = new ArrayList<>();
        for (FollowedTeamJson j : raw) {
            if (j.idTeam != null && j.name != null) {
                out.add(new FollowedTeam(j.idTeam, j.name, j.badgeUrl));
            }
        }
        return out;
    }

    public void addTeam(FollowedTeam team) {
        List<FollowedTeam> list = new ArrayList<>(getFollowedTeams());
        for (FollowedTeam t : list) {
            if (t.getIdTeam().equals(team.getIdTeam())) {
                return;
            }
        }
        list.add(team);
        save(list);
    }

    public void removeTeam(String idTeam) {
        List<FollowedTeam> list = new ArrayList<>(getFollowedTeams());
        list.removeIf(t -> t.getIdTeam().equals(idTeam));
        save(list);
    }

    private void save(List<FollowedTeam> teams) {
        List<FollowedTeamJson> jsonList = new ArrayList<>();
        for (FollowedTeam t : teams) {
            FollowedTeamJson j = new FollowedTeamJson();
            j.idTeam = t.getIdTeam();
            j.name = t.getName();
            j.badgeUrl = t.getBadgeUrl();
            jsonList.add(j);
        }
        prefs.edit().putString(KEY_TEAMS, gson.toJson(jsonList)).apply();
    }

    private static class FollowedTeamJson {
        String idTeam;
        String name;
        String badgeUrl;
    }
}
