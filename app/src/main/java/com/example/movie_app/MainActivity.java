package com.example.movie_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import com.example.movie_app.Model.MoviesModel;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    private ArrayList<MoviesModel> movies;
    private GridView gridView;

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

        getMovies();
        fragmentChanger(MenuFragment.class);

        initGui();
    }

    void initGui() {
        findViewById(R.id.btn_movies).setOnClickListener(v -> fragmentChanger(MovieFragment.class));
        findViewById(R.id.btn_series).setOnClickListener(v -> fragmentChanger(SeriesFragment.class));
        findViewById(R.id.btn_watchlist).setOnClickListener(v -> fragmentChanger(WatchlistFragment.class));
        findViewById(R.id.btn_search).setOnClickListener(v -> { });
    }

    void getMovies() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", Secrets.Token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // Handle failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    // Convert JSON to a single MoviesModel object
                    convertToMoviesModel(json);
                    // Save the entire JSON response in SharedPreferences
                    saveMovies(json);
                } else {
                    Log.wtf("Nothing works!!", response.message());
                    // Handle unsuccessful response
                    // Maybe show an error message to the user
                }
            }
        });
    }

    void saveMovies(String data){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("movies", data);
        editor.apply();
    }

    void convertToMoviesModel(String json){
        // Parse the JSON string into a single MoviesModel object
        MoviesModel moviesModel = new Gson().fromJson(json, MoviesModel.class);
        // Process the MoviesModel object as needed
        // For example, you can extract the list of movies from moviesModel.getResults()
        // and use it to populate your UI
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            showCloseAppDialog();
        }
    }

    private void showCloseAppDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Going back further will close the app. Are you sure you want to continue?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    // Close the app
                    finish();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    // Dismiss the dialog
                    dialogInterface.dismiss();
                })
                .show();
    }
}