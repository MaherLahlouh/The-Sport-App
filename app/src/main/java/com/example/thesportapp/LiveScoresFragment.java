
package com.example.thesportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.thesportapp.model.EventsResponse;
import com.example.thesportapp.model.MatchEvent;
import com.example.thesportapp.network.ApiClient;
import com.example.thesportapp.network.TheSportsDbApi;
import com.example.thesportapp.ui.MatchesAdapter;
import com.example.thesportapp.util.MatchDialogHelper;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveScoresFragment extends Fragment {

    private final Calendar day = Calendar.getInstance();
    private String sport = "Soccer";
    private TheSportsDbApi api;
    private MatchesAdapter adapter;
    private TextView textDate;
    private TextView textEmpty;
    private SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_live, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        api = ApiClient.get();
        textDate = view.findViewById(R.id.textDate);
        textEmpty = view.findViewById(R.id.textEmpty);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        RecyclerView recycler = view.findViewById(R.id.recyclerMatches);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MatchesAdapter(e -> MatchDialogHelper.show(requireContext(), e));
        recycler.setAdapter(adapter);

        ChipGroup chipGroup = view.findViewById(R.id.chipGroupSport);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                group.check(R.id.chipSoccer);
                return;
            }
            int id = checkedIds.get(0);
            if (id == R.id.chipSoccer) {
                sport = "Soccer";
            } else if (id == R.id.chipBasketball) {
                sport = "Basketball";
            }
            loadMatches();
        });

        view.findViewById(R.id.buttonPrevDay).setOnClickListener(v -> {
            day.add(Calendar.DAY_OF_MONTH, -1);
            updateDateLabel();
            loadMatches();
        });
        view.findViewById(R.id.buttonNextDay).setOnClickListener(v -> {
            day.add(Calendar.DAY_OF_MONTH, 1);
            updateDateLabel();
            loadMatches();
        });

        swipeRefresh.setOnRefreshListener(this::loadMatches);
        swipeRefresh.setColorSchemeResources(R.color.accent_green);
        updateDateLabel();
        loadMatches();
    }

    private void updateDateLabel() {
        SimpleDateFormat readable = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        textDate.setText(readable.format(day.getTime()));
    }

    private String isoDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        fmt.setTimeZone(TimeZone.getDefault());
        return fmt.format(day.getTime());
    }

    private void loadMatches() {
        swipeRefresh.setRefreshing(true);
        textEmpty.setVisibility(View.GONE);
        api.getEventsForDay(isoDate(), sport).enqueue(new Callback<EventsResponse>() {
            @Override
            public void onResponse(@NonNull Call<EventsResponse> call,
                                   @NonNull Response<EventsResponse> response) {
                swipeRefresh.setRefreshing(false);
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    List<MatchEvent> list = response.body().getEvents();
                    adapter.setItems(list);
                    if (list.isEmpty()) {
                        textEmpty.setText(R.string.empty_matches);
                        textEmpty.setVisibility(View.VISIBLE);
                    } else {
                        textEmpty.setVisibility(View.GONE);
                    }
                } else {
                    adapter.setItems(null);
                    textEmpty.setVisibility(View.VISIBLE);
                    textEmpty.setText(R.string.error_network);
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventsResponse> call, @NonNull Throwable t) {
                swipeRefresh.setRefreshing(false);
                if (!isAdded()) {
                    return;
                }
                adapter.setItems(null);
                textEmpty.setVisibility(View.VISIBLE);
                textEmpty.setText(R.string.error_network);
            }
        });
    }
}
