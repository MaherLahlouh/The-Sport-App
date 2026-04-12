package com.example.thesportapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thesportapp.R;
import com.example.thesportapp.model.MatchEvent;

import java.util.ArrayList;
import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.VH> {

    public interface OnMatchClickListener {
        void onMatchClick(MatchEvent event);
    }

    private final List<MatchEvent> data = new ArrayList<>();
    private final OnMatchClickListener listener;

    public MatchesAdapter(OnMatchClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<MatchEvent> items) {
        data.clear();
        if (items != null) {
            data.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MatchEvent e = data.get(position);
        holder.league.setText(e.getStrLeague());
        holder.homeName.setText(e.getStrHomeTeam());
        holder.awayName.setText(e.getStrAwayTeam());
        if (e.hasScore()) {
            holder.score.setText(e.getIntHomeScore() + " - " + e.getIntAwayScore());
        } else {
            holder.score.setText("vs");
        }
        String time = e.getStrTimeLocal();
        if (time == null || time.isEmpty()) {
            time = e.getStrTime();
        }
        if (time != null && time.length() >= 5) {
            holder.time.setText(time.substring(0, 5));
        } else {
            holder.time.setText("");
        }
        String status = e.getStrStatus();
        holder.status.setText(status != null ? status : "");
        loadBadge(holder.homeBadge, e.getStrHomeTeamBadge());
        loadBadge(holder.awayBadge, e.getStrAwayTeamBadge());
        loadBadge(holder.leagueBadge, e.getStrLeagueBadge());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMatchClick(e);
            }
        });
    }

    private static void loadBadge(ImageView iv, String url) {
        if (url == null || url.isEmpty()) {
            iv.setImageResource(R.drawable.ic_launcher_foreground);
            return;
        }
        Glide.with(iv.getContext()).load(url).fitCenter().into(iv);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView league;
        final TextView homeName;
        final TextView awayName;
        final TextView score;
        final TextView time;
        final TextView status;
        final ImageView homeBadge;
        final ImageView awayBadge;
        final ImageView leagueBadge;

        VH(@NonNull View itemView) {
            super(itemView);
            league = itemView.findViewById(R.id.textLeague);
            homeName = itemView.findViewById(R.id.textHome);
            awayName = itemView.findViewById(R.id.textAway);
            score = itemView.findViewById(R.id.textScore);
            time = itemView.findViewById(R.id.textTime);
            status = itemView.findViewById(R.id.textStatus);
            homeBadge = itemView.findViewById(R.id.imageHomeBadge);
            awayBadge = itemView.findViewById(R.id.imageAwayBadge);
            leagueBadge = itemView.findViewById(R.id.imageLeagueBadge);
        }
    }
}
