package com.example.movie_app.Adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.movie_app.DetailedMovieFragment;
import com.example.movie_app.Model.MoviesModel;
import com.example.movie_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends ArrayAdapter<MoviesModel.Result> {

    private Context context;

    public SearchAdapter(Context context, List<MoviesModel.Result> movies) {
        super(context, 0, movies);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        MovieAdapter.ViewHolder viewHolder;

        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            viewHolder = new MovieAdapter.ViewHolder();

            // Inflate the appropriate layout based on the device orientation
            if (this.context.getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                listItemView = inflater.inflate(R.layout.grid_item_landscape, parent, false);
            } else {
                listItemView = inflater.inflate(R.layout.grid_item_portrait, parent, false);
            }

            viewHolder.imageView = listItemView.findViewById(R.id.iv_imageView); // Example: Replace 'iv_imageView' with the id of your ImageView in grid item layout

            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (MovieAdapter.ViewHolder) listItemView.getTag();
        }

        // Bind data to your grid item layout here
        MoviesModel.Result result = getItem(position);

        // Load backdrop image using Picasso
        if (result.getBackdrop_path() != null) {
            String imageUrl = "https://image.tmdb.org/t/p/original/" + result.getPoster_path();
            Picasso.get().load(imageUrl).into(viewHolder.imageView);
        }

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the selected movie directly to DetailedMovieFragment.newInstance()
                DetailedMovieFragment fragment = DetailedMovieFragment.newInstance(result);

                // Get the fragment manager and start the transaction
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack("name")
                        .commit();
            }
        });

        return listItemView;
    }
}
