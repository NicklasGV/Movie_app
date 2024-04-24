package com.example.movie_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movie_app.Model.MoviesModel;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rq = Volley.newRequestQueue(this);

//        getMovies();
        fragmentChanger(MenuFragment.class);

        initGui();
    }

    void initGui() {
        findViewById(R.id.btn_movies).setOnClickListener(v -> fragmentChanger(MovieFragment.class));
        findViewById(R.id.btn_series).setOnClickListener(v -> fragmentChanger(SeriesFragment.class));
        findViewById(R.id.btn_watchlist).setOnClickListener(v -> fragmentChanger(WatchlistFragment.class));
        findViewById(R.id.btn_search).setOnClickListener(v -> fragmentChanger(SearchFragment.class));
    }

    private void fragmentChanger(Class c) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, c, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}