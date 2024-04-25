package com.example.movie_app;

import static com.example.movie_app.MainActivity.rq;

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
import android.widget.AbsListView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movie_app.Adapter.MovieAdapter;
import com.example.movie_app.Model.MoviesModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieFragment extends Fragment {
    int page = 0;
    private GridView gridView;
    public OnLoadMoreListener onLoadMoreListener;

    private ArrayList<MoviesModel.Result> results;
    private MovieAdapter adapter;
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
        rq = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        gridView = view.findViewById(R.id.gv_movielist);
        results = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), results);
        gridView.setAdapter(adapter);

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    // You've reached the end of the GridView, load more data here
                    getMovies();
                }
            }
        });

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

    private void loadMoviesFromPreferences() {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("movies", "");
        MoviesModel moviesModel = gson.fromJson(json, MoviesModel.class);
        Log.v("json data", json);

        if (moviesModel != null && moviesModel.getResults() != null) {
            // Add new data to the existing list
            results.addAll(moviesModel.getResults());
            adapter.notifyDataSetChanged(); // Notify adapter of data change
        }
    }

    public void getMovies() {
        page = page + 1;
        String url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=" + page + "&sort_by=popularity.desc";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            String json = response;
            // Save the entire JSON response in SharedPreferences
            saveMovies(json);
            loadMoviesFromPreferences();
        }, error -> Log.e("Volley", error.toString()))
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
}