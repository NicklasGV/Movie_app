package com.example.movie_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movie_app.Model.MoviesModel;
import com.squareup.picasso.Picasso;

public class DetailedMovieFragment extends Fragment {

    private MoviesModel.Result mSelectedMovie;

    public DetailedMovieFragment() {
        // Required empty public constructor
    }

    public static DetailedMovieFragment newInstance(MoviesModel.Result selectedMovie) {
        DetailedMovieFragment fragment = new DetailedMovieFragment();
        Bundle args = new Bundle();
        args.putSerializable("selectedMovie", selectedMovie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedMovie = (MoviesModel.Result) getArguments().getSerializable("selectedMovie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailed_movie, container, false);

        // Display detailed information about the selected movie
        if (mSelectedMovie != null) {
            TextView titleTextView = view.findViewById(R.id.tv_detailTitle);
            ImageView posterImage = view.findViewById(R.id.iv_poster);
            TextView releaseDate = view.findViewById(R.id.tv_releaseDate);
            TextView overView = view.findViewById(R.id.tv_overview);

            titleTextView.setText(mSelectedMovie.getTitle());
            String imageUrl = "https://image.tmdb.org/t/p/original/" + mSelectedMovie.getBackdrop_path();
            Picasso.get().load(imageUrl).into(posterImage);
            releaseDate.setText(mSelectedMovie.getRelease_date());
            overView.setText(mSelectedMovie.getOverview());



            // You can continue displaying other details of the movie (e.g., release date, rating) here
        }

        return view;
    }
}