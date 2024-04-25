package com.example.movie_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.example.movie_app.Adapter.WatchlistAdapter;
import com.example.movie_app.Database.MovieDBHelper;
import com.example.movie_app.Model.MoviesModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class WatchlistFragment extends Fragment {
    private GridView gridView;

    public WatchlistFragment() {
        // Required empty public constructor
    }
    public static WatchlistFragment newInstance() {
        WatchlistFragment fragment = new WatchlistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        // Retrieve data from preferences
        SharedPreferences preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("saved_movies", "");
        MoviesModel moviesModel = gson.fromJson(json, MoviesModel.class);

        ArrayList<MoviesModel.Result> results = new ArrayList<>();
        if (moviesModel != null && moviesModel.getResults() != null) {
            results.addAll(moviesModel.getResults());
        }

        gridView = view.findViewById(R.id.gv_watchlist);
        updateGridViewColumns();

        gridView.setAdapter(new WatchlistAdapter(getContext(), results));

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Call updateGridViewColumns when configuration changes (e.g., device orientation)
        setRetainInstance(true);
        updateGridViewColumns();
    }

    private void updateGridViewColumns() {
        int columns;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidthPx = displayMetrics.widthPixels;
        int screenHeightPx = displayMetrics.heightPixels;
        float screenDensity = displayMetrics.density;

        // Define thresholds for different screen sizes
        int phoneScreenWidthThreshold = 1000; // in pixels
        int tabletScreenWidthThreshold = 1400; // in pixelsw

        // Calculate screen width in dp
        int screenWidthDp = (int) (screenWidthPx / screenDensity);

        // Determine number of columns based on screen size
        if (screenWidthDp < phoneScreenWidthThreshold) {
            columns = 2; // Small screens (phones)
        } else if (screenWidthDp < tabletScreenWidthThreshold) {
            columns = 3; // Medium screens (tablets)
        } else {
            columns = 4; // Large screens (PC screens)
        }

        gridView.setNumColumns(columns);
    }
}