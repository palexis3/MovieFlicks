package com.example.palexis3.movieflicks.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.palexis3.movieflicks.Models.Movie;
import com.example.palexis3.movieflicks.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailActivity extends AppCompatActivity {

    ImageView ivMovie;
    TextView tvTitle;
    TextView tvReleaseDate;
    TextView tvOverView;
    String key;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //get the movie passed in from the intent
        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        key = getIntent().getStringExtra("key");

        // initialize all of the views
        ivMovie = (ImageView) findViewById(R.id.ivMovieDetailImage);
        tvTitle = (TextView) findViewById(R.id.tvMovieDetailTitle);
        tvReleaseDate = (TextView) findViewById(R.id.tvMovieDetailReleaseDate);
        tvOverView = (TextView) findViewById(R.id.tvMovieDetailOverview);

        // parsing release date from movie
        String[] arr = movie.getReleaseDate().split("-");
        String release_date = String.format("%s-%s-%s", arr[1], arr[2], arr[0]);

        // load other data
        tvTitle.setText(movie.getOriginalTitle());
        tvReleaseDate.setText(release_date);
        tvOverView.setText(movie.getOverView());

        // load image with picasso
        Picasso.with(MovieDetailActivity.this).load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(10,10)).into(ivMovie);

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
