package com.example.thesportapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thesportapp.data.PopularLeagues;
import com.example.thesportapp.ui.LeaguesAdapter;

public class LeaguesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leagues, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recycler = view.findViewById(R.id.recyclerLeagues);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        LeaguesAdapter adapter = new LeaguesAdapter(league -> {
            Intent i = new Intent(requireContext(), LeagueDetailActivity.class);
            i.putExtra(LeagueDetailActivity.EXTRA_LEAGUE_ID, league.getId());
            i.putExtra(LeagueDetailActivity.EXTRA_LEAGUE_NAME, league.getName());
            startActivity(i);
        });
        adapter.setItems(PopularLeagues.all());
        recycler.setAdapter(adapter);
    }
}
