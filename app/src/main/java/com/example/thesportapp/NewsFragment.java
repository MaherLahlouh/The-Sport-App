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

import com.example.thesportapp.model.NewsItem;
import com.example.thesportapp.ui.NewsAdapter;
import com.example.thesportapp.util.RssFetcher;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsFragment extends Fragment {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private NewsAdapter adapter;
    private TextView textEmpty;
    private SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textEmpty = view.findViewById(R.id.textEmpty);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        RecyclerView recycler = view.findViewById(R.id.recyclerNews);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new NewsAdapter();
        recycler.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::loadNews);
        swipeRefresh.setColorSchemeResources(R.color.accent_green);
        loadNews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executor.shutdownNow();
    }

    private void loadNews() {
        swipeRefresh.setRefreshing(true);
        textEmpty.setVisibility(View.GONE);
        executor.execute(() -> {
            try {
                List<NewsItem> items = RssFetcher.fetchFootballHeadlines();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (!isAdded()) {
                            return;
                        }
                        swipeRefresh.setRefreshing(false);
                        adapter.setItems(items);
                        textEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
                    });
                }
            } catch (Exception e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (!isAdded()) {
                            return;
                        }
                        swipeRefresh.setRefreshing(false);
                        adapter.setItems(null);
                        textEmpty.setVisibility(View.VISIBLE);
                        textEmpty.setText(R.string.error_network);
                    });
                }
            }
        });
    }
}
