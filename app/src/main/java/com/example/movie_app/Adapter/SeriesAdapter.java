package com.example.movie_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.example.movie_app.DetailedSeriesFragment;
import com.example.movie_app.MainActivity;
import com.example.movie_app.Model.SeriesModel;
import com.example.movie_app.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SeriesAdapter extends ArrayAdapter<SeriesModel.Result> {
    private Context context;
    private ArrayList<SeriesModel.Result> sResults;

    public SeriesAdapter(Context context, ArrayList<SeriesModel.Result> results) {
        super(context, 0, results);
        this.context = context;
        sResults = results;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        SeriesAdapter.ViewHolder viewHolder;

        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            viewHolder = new SeriesAdapter.ViewHolder();

            // Inflate the appropriate layout based on the device orientation
            if (this.context.getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                listItemView = inflater.inflate(R.layout.grid_item_landscape, parent, false);
            } else {
                listItemView = inflater.inflate(R.layout.grid_item_portrait, parent, false);
            }

            viewHolder.imageView = listItemView.findViewById(R.id.iv_imageView);
            viewHolder.btn_thumb = listItemView.findViewById(R.id.btn_thumb); // Initialize btn_thumb here

            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        // Bind data to your grid item layout here
        SeriesModel.Result result = sResults.get(position);

        // Load backdrop image using Picasso
        if (result.getBackdrop_path() != null) {
            String imageUrl = "https://image.tmdb.org/t/p/original/" + result.getPoster_path();
            Picasso.get().load(imageUrl).into(viewHolder.imageView);
        }

        viewHolder.imageView.setOnClickListener(v -> {
            // Pass the selected series directly to DetailedSeriesFragment.newInstance()
            DetailedSeriesFragment fragment = DetailedSeriesFragment.newInstance(result);

            // Get the fragment manager and start the transaction
            FragmentManager fragmentManager = ((AppCompatActivity) this.context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("name")
                    .commit();
        });

        viewHolder.btn_thumb.setOnClickListener(v -> {
            String json = new Gson().toJson(result);
            SharedPreferences sharedPref = ((MainActivity) context).getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("saved_Series", json);
            editor.apply();
            Toast.makeText(context, "Series liked", Toast.LENGTH_SHORT).show();
        });

        return listItemView;
    }

    static class ViewHolder {
        ImageView imageView;
        ImageButton btn_thumb;
    }
}
