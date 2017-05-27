package com.example.palexis3.movieflicks.Activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Models.NearbyMovies;
import com.example.palexis3.movieflicks.R;
import com.example.palexis3.movieflicks.Utilities;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearbyMovieDetailActivity extends AppCompatActivity {

    // setup the butterknife binding
    @BindView(R.id.ivCardViewNearbyMovieImage) ImageView movieImage;
    @BindView(R.id.tvCardViewNearbyMovieTitle) TextView movieTitle;
    @BindView(R.id.tvCardViewMovieRating) TextView movieRating;
    @BindView(R.id.tvCardViewUrl) TextView movieUrl;
    @BindView(R.id.tvCardViewNearbyMovieReleaseDate) TextView movieReleaseDate;
    @BindView(R.id.tvCardViewNearbyMovieCast) TextView movieCast;
    @BindView(R.id.tvCardViewNearbyMovieGenre) TextView movieGenre;
    @BindView(R.id.tvCardViewNearbyMovieDescription) TextView movieDescription;
    @BindView(R.id.buttonNearbyMovie) Button showTimes;
    @BindView(R.id.detail_toolbar) Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // get the nearby movie passed in from intent from nearby movie recycler adapter
        final NearbyMovies movie = (NearbyMovies) Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        movieTitle.setText(movie.getTitle());
        movieRating.setText(String.format("Rated: %s", movie.getMovieRating()));
        movieUrl.setText(movie.getUrl());
        movieCast.setText(Utilities.convertToString(movie.getCast()));
        movieGenre.setText(Utilities.convertToString(movie.getGenres()));
        movieDescription.setText(movie.getDescription());
        movieDescription.setMovementMethod(new ScrollingMovementMethod());
        movieReleaseDate.setText(Utilities.parseDate(movie.getReleaseDate()));

        // load image
        Picasso.with(NearbyMovieDetailActivity.this).load(movie.getImagePath()).into(movieImage);

        showTimes.setText(String.format("%d SHOW TIMES", movie.getShowtimesList().size()));
        showTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // construct a dialog fragment that shows a listview of showtimes
                /** TODO: Create ShowTimesDialogFragment and pass in the showtimes list as an argument */
                //Bundle bundle = new Bundle();
                //bundle.putParcelableArrayList("showtimes", movie.getShowtimesList());
                Toast.makeText(getApplicationContext(), "Now get the showtimes!", Toast.LENGTH_LONG).show();
            }
        });

    }
}
