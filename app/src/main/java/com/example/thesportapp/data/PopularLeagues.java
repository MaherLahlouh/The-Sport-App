package com.example.thesportapp.data;

import com.example.thesportapp.model.PopularLeague;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class PopularLeagues {

    private PopularLeagues() {
    }

    private static final PopularLeague[] RAW = {
            new PopularLeague("4328", "English Premier League", "Soccer"),
            new PopularLeague("4332", "Italian Serie A", "Soccer"),
            new PopularLeague("4335", "Spanish La Liga", "Soccer"),
            new PopularLeague("4331", "German Bundesliga", "Soccer"),
            new PopularLeague("4334", "French Ligue 1", "Soccer"),
            new PopularLeague("4387", "NBA", "Basketball"),
            new PopularLeague("4433", "Italian Lega Basket", "Basketball"),
    };

    public static List<PopularLeague> all() {
        return Collections.unmodifiableList(Arrays.asList(RAW));
    }
}
