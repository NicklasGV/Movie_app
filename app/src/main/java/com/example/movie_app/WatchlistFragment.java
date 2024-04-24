package com.example.movie_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.movie_app.Adapter.MovieAdapter;
import com.example.movie_app.Model.MoviesModel;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        // Retrieve data from preferences
        SharedPreferences preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("saved_movies", "");
        MoviesModel moviesModel = gson.fromJson(json, MoviesModel.class);

        ArrayList<MoviesModel.Result> results = new ArrayList<>();
        if (moviesModel != null && moviesModel.getResults() != null) {
            results.addAll(moviesModel.getResults());
        }

        gridView = view.findViewById(R.id.gv_movielist);
        updateGridViewColumns(getResources().getConfiguration().orientation);
        gridView.setAdapter(new MovieAdapter(getContext(), results));

        return view;
    }

    private void updateGridViewColumns(int orientation) {
        int columns = 2; // Default to 2 columns for portrait orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 3; // Use 3 columns for landscape orientation
        }
        gridView.setNumColumns(columns);
    }
}