package com.example.movie_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.movie_app.Adapter.MovieAdapter;
import com.example.movie_app.Model.MoviesModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(String param1, String param2) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        // Retrieve data from preferences
        SharedPreferences preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("movies", "");
        MoviesModel moviesModel = gson.fromJson(json, MoviesModel.class);
        Log.v("json data", json);

        ArrayList<MoviesModel.Result> results = new ArrayList<>();
        if (moviesModel != null) {
            results.addAll(moviesModel.getResults());
        }

        gridView = view.findViewById(R.id.gv_movielist);
        updateGridViewColumns(getResources().getConfiguration().orientation);
        gridView.setAdapter(new MovieAdapter(getContext(), results));

        // Listen for changes in device orientation
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int orientation = getResources().getConfiguration().orientation;
            updateGridViewColumns(orientation);
        });

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