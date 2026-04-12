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
import com.example.thesportapp.model.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamSearchAdapter extends RecyclerView.Adapter<TeamSearchAdapter.VH> {

    public interface OnTeamClickListener {
        void onTeamClick(Team team);
    }

    private final List<Team> data = new ArrayList<>();
    private final OnTeamClickListener listener;

    public TeamSearchAdapter(OnTeamClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Team> items) {
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
                .inflate(R.layout.item_team_search, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Team t = data.get(position);
        holder.name.setText(t.getStrTeam());
        String sport = t.getStrSport();
        holder.sport.setText(sport != null ? sport : "");
        String badge = t.getStrTeamBadge();
        if (badge == null || badge.isEmpty()) {
            holder.badge.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            Glide.with(holder.badge.getContext()).load(badge).fitCenter().into(holder.badge);
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTeamClick(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView sport;
        final ImageView badge;

        VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textTeamName);
            sport = itemView.findViewById(R.id.textTeamSport);
            badge = itemView.findViewById(R.id.imageTeamBadge);
        }
    }
}
