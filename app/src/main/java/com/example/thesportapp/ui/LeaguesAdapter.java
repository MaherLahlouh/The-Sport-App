package com.example.thesportapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thesportapp.R;
import com.example.thesportapp.model.PopularLeague;

import java.util.ArrayList;
import java.util.List;

public class LeaguesAdapter extends RecyclerView.Adapter<LeaguesAdapter.VH> {

    public interface OnLeagueClickListener {
        void onLeagueClick(PopularLeague league);
    }

    private final List<PopularLeague> data = new ArrayList<>();
    private final OnLeagueClickListener listener;

    public LeaguesAdapter(OnLeagueClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<PopularLeague> items) {
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
                .inflate(R.layout.item_league, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PopularLeague l = data.get(position);
        holder.name.setText(l.getName());
        holder.sport.setText(l.getSport());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLeagueClick(l);
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

        VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textLeagueName);
            sport = itemView.findViewById(R.id.textLeagueSport);
        }
    }
}
