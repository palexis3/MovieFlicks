package com.example.palexis3.movieflicks.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Models.TvShows;
import com.example.palexis3.movieflicks.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/** TODO: Implement a default "Not Found" for pictures, also check out Glide for picture performance */

public class PopularTvShowsDetailActivity extends AppCompatActivity {

    // setup the butterknife binding
    @BindView(R.id.ivTvShowBackdropImage) ImageView backdropImage;
    @BindView(R.id.ivTvShowPosterPathImage) ImageView posterImage;
    @BindView(R.id.tvTvShowDetailTitle) TextView title;
    @BindView(R.id.tvTvShowDetailHomepageUrl) TextView url;
    @BindView(R.id.tvTvShowDetailLastAirDate) TextView lastAirDate;
    @BindView(R.id.tvTvShowDetailRatingBar) TextView ratingBar;
    @BindView(R.id.tvTvShowDetailGenre) TextView genres;
    @BindView(R.id.tvTvShowDetailNetworks) TextView networks;
    @BindView(R.id.tvTvShowDetailSeasons) TextView seasons;
    @BindView(R.id.tvTvShowDetailEpisodes) TextView episodes;
    @BindView(R.id.tvTvShowDetailOverview) TextView overView;
    @BindView(R.id.detail_toolbar) Toolbar toolbar;

    private String key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_tvshows_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // get the show passed in from the popular tv show recycler adapter
        TvShows show = (TvShows) Parcels.unwrap(getIntent().getParcelableExtra("show"));
        key = (show.getYoutubeKey() != null) ? show.getYoutubeKey() : null;

        // parse last air date
        String date = parseDate(show.getLastAirDate());
        lastAirDate.setText(date);

        // load other data
        title.setText(show.getName());
        url.setText(show.getHomepage_url());
        ratingBar.setText(String.valueOf(show.getRating()));
        seasons.setText(String.valueOf(show.getNumOfSeasons()));
        episodes.setText(String.valueOf(show.getNumOfEpisodes()));
        overView.setText(show.getOverView());
        overView.setMovementMethod(new ScrollingMovementMethod());

        String list_genres = convertToString(show.getGenres());
        genres.setText(list_genres);

        String list_networks = convertToString(show.getNetworks());
        networks.setText(list_networks);

        // load images
        Picasso.with(PopularTvShowsDetailActivity.this).load(show.getPosterPath()).into(posterImage);
        Picasso.with(PopularTvShowsDetailActivity.this).load(show.getBackdropPath()).into(backdropImage);

        // listener waits for the movie to be clicked to launch youtube activity
        movieImageListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    private void movieImageListener() {
        backdropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(key != null) {
                    // start the youtube viewer intent
                    Intent i = new Intent(PopularTvShowsDetailActivity.this, QuickPlayActivity.class);
                    i.putExtra("key", key);
                    startActivity(i);
                } else{
                    Toast.makeText(PopularTvShowsDetailActivity.this, "Error: this show doesn't have a youtube key!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // converts a list to string
    private static String convertToString(List<String> list) {
        if(list == null || list.size() == 0) return "N/A";

        StringBuilder sb = new StringBuilder();
        sb.append(list.get(0));

        for(int i = 1; i < list.size(); i++) {
            sb.append(", ");
            sb.append(list.get(i));
        }

        return sb.toString();
    }

    // converting numerical date to string based date
    private static String parseDate(String inputDate) {

        if(inputDate == null || inputDate.length() == 0) return "N/A";

        HashMap<String, String> map = new HashMap<>();

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


        String[] arr = inputDate.split("-");
        String month = arr[1].charAt(0) == '0' ? arr[1].substring(1) : arr[1];
        String res = String.format("%s %s, %s", map.get(month), arr[2], arr[0]);
        return res;
    }
}
