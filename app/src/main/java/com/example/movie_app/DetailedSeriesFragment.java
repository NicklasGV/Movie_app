package com.example.movie_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movie_app.Database.MovieDBHelper;
import com.example.movie_app.Model.MoviesModel;
import com.example.movie_app.Model.SeriesModel;
import com.squareup.picasso.Picasso;

public class DetailedSeriesFragment extends Fragment {
    private SeriesModel.Result mSelectedSerie;

    public DetailedSeriesFragment() {
        // Required empty public constructor
    }

    public static DetailedSeriesFragment newInstance(SeriesModel.Result selectedSeries) {
        DetailedSeriesFragment fragment = new DetailedSeriesFragment();
        Bundle args = new Bundle();
        args.putSerializable("selectedSeries", selectedSeries);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedSerie = (SeriesModel.Result) getArguments().getSerializable("selectedSeries");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailed_series, container, false);

        // Display detailed information about the selected series
        if (mSelectedSerie != null) {
            TextView titleTextView = view.findViewById(R.id.tv_detailTitle);
            ImageView posterImage = view.findViewById(R.id.iv_poster);
            TextView releaseDate = view.findViewById(R.id.tv_releaseDate);
            TextView overView = view.findViewById(R.id.tv_overview);

            titleTextView.setText(mSelectedSerie.getName());
            String imageUrl = "https://image.tmdb.org/t/p/original/" + mSelectedSerie.getBackdrop_path();
            Picasso.get().load(imageUrl).into(posterImage);
            releaseDate.setText(mSelectedSerie.getFirst_air_date());
            overView.setText(mSelectedSerie.getOverview());
        }

        return view;
    }
}