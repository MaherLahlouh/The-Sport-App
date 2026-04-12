package com.example.thesportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.thesportapp.model.EventsResponse;
import com.example.thesportapp.model.FollowedTeam;
import com.example.thesportapp.model.MatchEvent;
import com.example.thesportapp.model.Team;
import com.example.thesportapp.model.TeamsResponse;
import com.example.thesportapp.network.ApiClient;
import com.example.thesportapp.network.TheSportsDbApi;
import com.example.thesportapp.ui.FollowedTeamAdapter;
import com.example.thesportapp.ui.MatchesAdapter;
import com.example.thesportapp.ui.TeamSearchAdapter;
import com.example.thesportapp.util.FollowStore;
import com.example.thesportapp.util.MatchDialogHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingFragment extends Fragment {

    private FollowStore followStore;
    private TheSportsDbApi api;
    private FollowedTeamAdapter followedAdapter;
    private MatchesAdapter fixturesAdapter;
    private TextView textEmpty;
    private SwipeRefreshLayout swipeRefresh;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        followStore = new FollowStore(requireContext());
        api = ApiClient.get();
        textEmpty = view.findViewById(R.id.textEmpty);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_add_team) {
                showSearchDialog();
                return true;
            }
            return false;
        });

        RecyclerView recyclerFollowed = view.findViewById(R.id.recyclerFollowed);
        LinearLayoutManager horizontal = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerFollowed.setLayoutManager(horizontal);
        followedAdapter = new FollowedTeamAdapter(team -> {
            followStore.removeTeam(team.getIdTeam());
            refreshFollowedUi();
            loadFixtures();
        });
        recyclerFollowed.setAdapter(followedAdapter);

        RecyclerView recyclerFixtures = view.findViewById(R.id.recyclerFixtures);
        recyclerFixtures.setLayoutManager(new LinearLayoutManager(requireContext()));
        fixturesAdapter = new MatchesAdapter(e -> MatchDialogHelper.show(requireContext(), e));
        recyclerFixtures.setAdapter(fixturesAdapter);

        swipeRefresh.setOnRefreshListener(this::loadFixtures);
        swipeRefresh.setColorSchemeResources(R.color.accent_green);
        refreshFollowedUi();
        loadFixtures();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executor.shutdownNow();
    }

    private void refreshFollowedUi() {
        followedAdapter.setItems(followStore.getFollowedTeams());
    }

    private void showSearchDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_team_search, null, false);
        TextInputEditText editSearch = dialogView.findViewById(R.id.editSearch);
        RecyclerView recyclerResults = dialogView.findViewById(R.id.recyclerResults);
        recyclerResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        TeamSearchAdapter searchAdapter = new TeamSearchAdapter(team -> {
            followStore.addTeam(new FollowedTeam(
                    team.getIdTeam(),
                    team.getStrTeam(),
                    team.getStrTeamBadge()));
            refreshFollowedUi();
            loadFixtures();
            Toast.makeText(requireContext(), R.string.add_follow, Toast.LENGTH_SHORT).show();
        });
        recyclerResults.setAdapter(searchAdapter);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.search_teams_hint)
                .setView(dialogView)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialogView.findViewById(R.id.buttonSearch).setOnClickListener(v -> {
            CharSequence q = editSearch.getText();
            if (q == null || q.toString().trim().isEmpty()) {
                return;
            }
            api.searchTeams(q.toString().trim()).enqueue(new Callback<TeamsResponse>() {
                @Override
                public void onResponse(@NonNull Call<TeamsResponse> call,
                                       @NonNull Response<TeamsResponse> response) {
                    if (!dialog.isShowing()) {
                        return;
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        searchAdapter.setItems(response.body().getTeams());
                    } else {
                        searchAdapter.setItems(null);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TeamsResponse> call, @NonNull Throwable t) {
                    if (dialog.isShowing()) {
                        searchAdapter.setItems(null);
                    }
                }
            });
        });

        dialog.show();
    }

    private void loadFixtures() {
        List<FollowedTeam> teams = followStore.getFollowedTeams();
        if (teams.isEmpty()) {
            swipeRefresh.setRefreshing(false);
            fixturesAdapter.setItems(null);
            textEmpty.setVisibility(View.VISIBLE);
            return;
        }
        swipeRefresh.setRefreshing(true);
        textEmpty.setVisibility(View.GONE);
        executor.execute(() -> {
            List<MatchEvent> merged = new ArrayList<>();
            for (FollowedTeam t : teams) {
                try {
                    Response<EventsResponse> res = api.getNextEventsForTeam(t.getIdTeam()).execute();
                    if (res.isSuccessful() && res.body() != null) {
                        merged.addAll(res.body().getEvents());
                    }
                } catch (Exception ignored) {
                    // skip team on failure
                }
            }
            Collections.sort(merged, Comparator.comparing(MatchEvent::getStrTimestamp,
                    Comparator.nullsLast(String::compareTo)));
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (!isAdded()) {
                        return;
                    }
                    swipeRefresh.setRefreshing(false);
                    fixturesAdapter.setItems(merged);
                    textEmpty.setVisibility(merged.isEmpty() ? View.VISIBLE : View.GONE);
                });
            }
        });
    }
}
