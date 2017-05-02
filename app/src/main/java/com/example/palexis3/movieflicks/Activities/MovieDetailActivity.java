package com.example.palexis3.movieflicks.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.palexis3.movieflicks.Models.Movie;
import com.example.palexis3.movieflicks.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailActivity extends AppCompatActivity {

    /*
    // set the number of total stars to show
                movieViewHolder.popularMovieRating.setNumStars(5);

    // set the rating bar of the movie
                movieViewHolder.popularMovieRating.setRating((float) movie.getRating() / 2);
    */

    // setting up butterknife binding
    @BindView(R.id.ivMovieDetailImage) ImageView ivMovie;
    @BindView(R.id.tvMovieDetailTitle) TextView tvTitle;
    @BindView(R.id.tvMovieDetailReleaseDate) TextView tvReleaseDate;
    @BindView(R.id.tvMovieDetailOverview) TextView tvOverView;
    @BindView(R.id.detail_toolbar) Toolbar toolbar;

    private String key;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        //get the movie passed in from the intent
        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        key = getIntent().getStringExtra("key");

        // parsing release date from movie
        String[] arr = movie.getReleaseDate().split("-");
        String release_date = String.format("%s-%s-%s", arr[1], arr[2], arr[0]);

        // load other data
        tvTitle.setText(movie.getOriginalTitle());
        tvReleaseDate.setText(release_date);
        tvOverView.setText(movie.getOverView());

        // load image with picasso
        Picasso.with(MovieDetailActivity.this).load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(7,7)).into(ivMovie);

        // listener waits for the image to be clicked to launch youtube player
        imageViewListener();
    }

    private void imageViewListener() {
        ivMovie.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // start the youtube viewer intent
                Intent i = new Intent(MovieDetailActivity.this, QuickPlayActivity.class);
                i.putExtra("key", key);
                startActivity(i);
            }
        });
    }
}
