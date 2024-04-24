package com.example.movie_app;

import static com.example.movie_app.MainActivity.rq;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.movie_app.Adapter.MovieAdapter;
import com.example.movie_app.Model.MoviesModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieFragment extends Fragment {
    private GridView gridView;
    private ImageButton imagebtn;
    public MovieFragment() {
        // Required empty public constructor
    }
    public static MovieFragment newInstance() {
        MovieFragment fragment = new MovieFragment();
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
        getMovies();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        // Retrieve data from preferences
        SharedPreferences preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("movies", "");
        MoviesModel moviesModel = gson.fromJson(json, MoviesModel.class);

        ArrayList<MoviesModel.Result> results = new ArrayList<>();
        if (moviesModel != null) {
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

    void getMovies() {
        String url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            String json = response;
            // Convert JSON to a single MoviesModel object
            convertToMoviesModel(json);
            // Save the entire JSON response in SharedPreferences
            saveMovies(json);

        }, volleyError -> {
            Log.wtf("WTFFF", volleyError.toString());
        })
        {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", Secrets.Token);
                return params;
            }
        };
        rq.add(request);
    }

    void saveMovies(String data){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("movies", data);
        editor.apply();
    }

    void convertToMoviesModel(String json){
        MoviesModel moviesModel = new Gson().fromJson(json, MoviesModel.class);
    }
}