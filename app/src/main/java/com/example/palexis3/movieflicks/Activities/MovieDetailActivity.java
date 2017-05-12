package com.example.palexis3.movieflicks.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Models.Movie;
import com.example.palexis3.movieflicks.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    // setting up butterknife binding
    @BindView(R.id.ivMovieDetailImage) ImageView ivMovie;
    @BindView(R.id.tvMovieDetailTitle) TextView tvTitle;
    @BindView(R.id.tvMovieDetailReleaseDate) TextView tvReleaseDate;
    @BindView(R.id.tvMovieDetailOverview) TextView tvOverView;
    @BindView(R.id.tvMovieDetailRatingBar) TextView tvRatingBar;
    @BindView(R.id.detail_toolbar) Toolbar toolbar;

    private String key;
    private HashMap<String, String> map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        //get the movie passed in from the intent
        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        key = (movie.getYoutubeKey() != null) ? movie.getYoutubeKey() : null;

        map = new HashMap<>();
        map.put("1", "January");
        map.put("2", "February");
        map.put("3", "March");
        map.put("4", "April");
        map.put("5", "May");
        map.put("6", "June");
        map.put("7", "July");
        map.put("8", "August");
        map.put("9", "September");
        map.put("10", "October");
        map.put("11", "November");
        map.put("12", "December");

        // parsing release date from movie
        String[] arr = movie.getReleaseDate().split("-");
        String month = arr[1].charAt(0) == '0' ? arr[1].substring(1) : arr[1];
        String release_date = String.format("%s %s, %s", map.get(month), arr[2], arr[0]);

        // load other data
        tvTitle.setText(movie.getOriginalTitle());
        tvReleaseDate.setText(release_date);
        tvOverView.setText(movie.getOverView());
        String rating = movie.getRating() > 0.0 ? String.valueOf(movie.getRating()) : "N/A";
        tvRatingBar.setText(rating);

        // load image with picasso
        Picasso.with(MovieDetailActivity.this).load(movie.getBackdropPath()).into(ivMovie);

        // listener waits for the image to be clicked to launch youtube player
        imageViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    private void imageViewListener() {
        ivMovie.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(key != null) {
                    // start the youtube viewer intent
                    Intent i = new Intent(MovieDetailActivity.this, QuickPlayActivity.class);
                    i.putExtra("key", key);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Error: This movie doesn't have a youtube key!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
