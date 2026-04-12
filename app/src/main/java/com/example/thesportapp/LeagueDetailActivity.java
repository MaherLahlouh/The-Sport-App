package com.example.thesportapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.thesportapp.model.EventsResponse;
import com.example.thesportapp.model.MatchEvent;
import com.example.thesportapp.network.ApiClient;
import com.example.thesportapp.network.TheSportsDbApi;
import com.example.thesportapp.ui.ScheduleAdapter;
import com.example.thesportapp.util.MatchDialogHelper;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeagueDetailActivity extends AppCompatActivity {

    public static final String EXTRA_LEAGUE_ID = "league_id";
    public static final String EXTRA_LEAGUE_NAME = "league_name";

    private ScheduleAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_detail);

        String leagueId = getIntent().getStringExtra(EXTRA_LEAGUE_ID);
        String leagueName = getIntent().getStringExtra(EXTRA_LEAGUE_NAME);
        if (leagueId == null) {
            finish();
            return;
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(leagueName != null ? leagueName : getString(R.string.nav_leagues));
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        swipeRefresh = findViewById(R.id.swipeRefresh);
        RecyclerView recycler = findViewById(R.id.recyclerSchedule);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter(e -> MatchDialogHelper.show(this, e));
        recycler.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(() -> loadSchedule(leagueId));
        swipeRefresh.setColorSchemeResources(R.color.accent_green);
        loadSchedule(leagueId);
    }

    private void loadSchedule(String leagueId) {
        swipeRefresh.setRefreshing(true);
        TheSportsDbApi api = ApiClient.get();
        List<MatchEvent> upcoming = new ArrayList<>();
        List<MatchEvent> past = new ArrayList<>();
        AtomicInteger pending = new AtomicInteger(2);

        Runnable onBatchDone = () -> {
            if (pending.decrementAndGet() == 0) {
                runOnUiThread(() -> {
                    swipeRefresh.setRefreshing(false);
                    adapter.setSchedule(upcoming, past);
                });
            }
        };

        api.getNextEventsForLeague(leagueId).enqueue(new Callback<EventsResponse>() {
            @Override
            public void onResponse(@NonNull Call<EventsResponse> call,
                                   @NonNull Response<EventsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    upcoming.addAll(response.body().getEvents());
                }
                onBatchDone.run();
            }

            @Override
            public void onFailure(@NonNull Call<EventsResponse> call, @NonNull Throwable t) {
                onBatchDone.run();
            }
        });

        api.getPastEventsForLeague(leagueId).enqueue(new Callback<EventsResponse>() {
            @Override
            public void onResponse(@NonNull Call<EventsResponse> call,
                                   @NonNull Response<EventsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    past.addAll(response.body().getEvents());
                }
                onBatchDone.run();
            }

            @Override
            public void onFailure(@NonNull Call<EventsResponse> call, @NonNull Throwable t) {
                onBatchDone.run();
            }
        });
    }
}
