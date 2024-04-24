package com.example.movie_app;

import static com.example.movie_app.MainActivity.rq;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.movie_app.Adapter.SearchAdapter;
import com.example.movie_app.Model.MoviesModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.btn_startsearch).setOnClickListener(view -> getMovieBySearch());

    }

    void getMovieBySearch() {
        String query = ((EditText)getView().findViewById(R.id.inp_search)).getText().toString();

        String url = "https://api.themoviedb.org/3/search/movie?query=" + query;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            MoviesModel movieSearch = new Gson().fromJson(response, MoviesModel.class);
            fillAdapter(movieSearch.results);

        }, error -> Log.e("Volley", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", Secrets.Token);
                return params;
            }
        };
        rq.add(request);
    }

    private void fillAdapter(ArrayList<MoviesModel.Result> results) {
        SearchAdapter adaptor = new SearchAdapter(getContext(), results);
        GridView gridView =  getView().findViewById(R.id.gv_searchResult);
        gridView.setAdapter(adaptor);
    }
}