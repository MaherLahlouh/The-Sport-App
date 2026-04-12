package com.example.thesportapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thesportapp.R;
import com.example.thesportapp.model.MatchEvent;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_MATCH = 1;

    private final List<Object> rows = new ArrayList<>();
    private final MatchesAdapter.OnMatchClickListener listener;

    public ScheduleAdapter(MatchesAdapter.OnMatchClickListener listener) {
        this.listener = listener;
    }

    public void setSchedule(List<MatchEvent> upcoming, List<MatchEvent> past) {
        rows.clear();
        rows.add("Upcoming");
        if (upcoming != null) {
            rows.addAll(upcoming);
        }
        rows.add("Recent results");
        if (past != null) {
            rows.addAll(past);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = rows.get(position);
        return o instanceof String ? TYPE_HEADER : TYPE_MATCH;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_schedule_header, parent, false);
            return new HeaderVH(v);
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new MatchVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object o = rows.get(position);
        if (holder instanceof HeaderVH) {
            ((HeaderVH) holder).title.setText((String) o);
        } else if (holder instanceof MatchVH) {
            MatchVH mh = (MatchVH) holder;
            MatchEvent e = (MatchEvent) o;
            mh.bind(e, listener);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    static class HeaderVH extends RecyclerView.ViewHolder {
        final TextView title;

        HeaderVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textHeader);
        }
    }

    static class MatchVH extends RecyclerView.ViewHolder {
        private final TextView league;
        private final TextView homeName;
        private final TextView awayName;
        private final TextView score;
        private final TextView time;
        private final TextView status;
        private final android.widget.ImageView homeBadge;
        private final android.widget.ImageView awayBadge;
        private final android.widget.ImageView leagueBadge;

        MatchVH(@NonNull View itemView) {
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

        void bind(MatchEvent e, MatchesAdapter.OnMatchClickListener listener) {
            league.setText(e.getStrLeague());
            homeName.setText(e.getStrHomeTeam());
            awayName.setText(e.getStrAwayTeam());
            if (e.hasScore()) {
                score.setText(e.getIntHomeScore() + " - " + e.getIntAwayScore());
            } else {
                score.setText("vs");
            }
            String t = e.getStrTimeLocal();
            if (t == null || t.isEmpty()) {
                t = e.getStrTime();
            }
            if (t != null && t.length() >= 5) {
                time.setText(t.substring(0, 5));
            } else {
                time.setText("");
            }
            String st = e.getStrStatus();
            status.setText(st != null ? st : "");
            loadBadge(homeBadge, e.getStrHomeTeamBadge());
            loadBadge(awayBadge, e.getStrAwayTeamBadge());
            loadBadge(leagueBadge, e.getStrLeagueBadge());
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMatchClick(e);
                }
            });
        }

        private static void loadBadge(android.widget.ImageView iv, String url) {
            if (url == null || url.isEmpty()) {
                iv.setImageResource(R.drawable.ic_launcher_foreground);
                return;
            }
            Glide.with(iv.getContext()).load(url).fitCenter().into(iv);
        }
    }
}
