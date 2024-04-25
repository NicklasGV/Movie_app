package com.example.movie_app.Adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.movie_app.DetailedMovieFragment;
import com.example.movie_app.Model.MoviesModel;
import com.example.movie_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WatchlistAdapter extends ArrayAdapter<MoviesModel.Result> {

        private Context mContext;
        private ArrayList<MoviesModel.Result> mResults;

        public WatchlistAdapter(Context context, ArrayList<MoviesModel.Result> results) {
                super(context, 0, results);
                mContext = context;
                mResults = results;
                }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View listItemView = convertView;
                ViewHolder viewHolder;

                if (listItemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                viewHolder = new ViewHolder();

                // Inflate the appropriate layout based on the device orientation
                if (mContext.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
                listItemView = inflater.inflate(R.layout.grid_item_landscape, parent, false);
                } else {
                listItemView = inflater.inflate(R.layout.grid_item_portrait, parent, false);
                }

                viewHolder.imageView = listItemView.findViewById(R.id.iv_imageView); // Example: Replace 'iv_imageView' with the id of your ImageView in grid item layout

                listItemView.setTag(viewHolder);
                } else {
                viewHolder = (ViewHolder) listItemView.getTag();
                }

                // Bind data to your grid item layout here
                MoviesModel.Result result = mResults.get(position);

                // Load backdrop image using Picasso
                if (result.getBackdrop_path() != null) {
                String imageUrl = "https://image.tmdb.org/t/p/original/" + result.getPoster_path();
                Picasso.get().load(imageUrl).into(viewHolder.imageView);
                }

                viewHolder.imageView.setOnClickListener(v -> {
                        // Pass the selected movie directly to DetailedMovieFragment.newInstance()
                        DetailedMovieFragment fragment = DetailedMovieFragment.newInstance(result);

                        // Get the fragment manager and start the transaction
                        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack("name")
                        .commit();
                        });

                        return listItemView;
                        }

        static class ViewHolder {
            ImageView imageView;
        }
}
