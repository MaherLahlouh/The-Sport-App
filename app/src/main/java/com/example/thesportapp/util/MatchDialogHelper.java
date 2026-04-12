package com.example.thesportapp.util;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.thesportapp.R;
import com.example.thesportapp.model.MatchEvent;

public final class MatchDialogHelper {

    private MatchDialogHelper() {
    }

    public static void show(Context context, MatchEvent e) {
        String date = e.getDateEvent() != null ? e.getDateEvent() : "";
        String time = e.getStrTimeLocal();
        if (time == null || time.isEmpty()) {
            time = e.getStrTime();
        }
        String venue = e.getStrVenue() != null ? e.getStrVenue() : "";
        String status = e.getStrStatus() != null ? e.getStrStatus() : "";
        String sport = e.getStrSport() != null ? e.getStrSport() : "";
        StringBuilder msg = new StringBuilder();
        msg.append(e.getStrHomeTeam()).append(" vs ").append(e.getStrAwayTeam()).append("\n\n");
        if (e.hasScore()) {
            msg.append(context.getString(R.string.match_details)).append(": ")
                    .append(e.getIntHomeScore()).append(" - ").append(e.getIntAwayScore()).append("\n");
        }
        msg.append(date).append(" ").append(time != null ? time : "").append("\n");
        if (!venue.isEmpty()) {
            msg.append(venue).append("\n");
        }
        if (!status.isEmpty()) {
            msg.append(status).append("\n");
        }
        if (!sport.isEmpty()) {
            msg.append(sport);
        }
        new AlertDialog.Builder(context)
                .setTitle(R.string.match_details)
                .setMessage(msg.toString().trim())
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
