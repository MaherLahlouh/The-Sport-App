package com.example.thesportapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thesportapp.R;
import com.example.thesportapp.model.FollowedTeam;

import java.util.ArrayList;
import java.util.List;

public class FollowedTeamAdapter extends RecyclerView.Adapter<FollowedTeamAdapter.VH> {

    public interface OnRemoveListener {
        void onRemove(FollowedTeam team);
    }

    private final List<FollowedTeam> data = new ArrayList<>();
    private final OnRemoveListener removeListener;

    public FollowedTeamAdapter(OnRemoveListener removeListener) {
        this.removeListener = removeListener;
    }

    public void setItems(List<FollowedTeam> items) {
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
                .inflate(R.layout.item_followed_team, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        FollowedTeam t = data.get(position);
        holder.name.setText(t.getName());
        String url = t.getBadgeUrl();
        if (url == null || url.isEmpty()) {
            holder.badge.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            Glide.with(holder.badge.getContext()).load(url).fitCenter().into(holder.badge);
        }
        holder.remove.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemove(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView name;
        final ImageView badge;
        final ImageButton remove;

        VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textFollowedName);
            badge = itemView.findViewById(R.id.imageFollowedBadge);
            remove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
