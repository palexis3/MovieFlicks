package com.example.palexis3.movieflicks.Activities;


import android.os.Bundle;
import android.widget.Toast;

import com.example.palexis3.movieflicks.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuickPlayActivity extends YouTubeBaseActivity{

    // using Butterknife for binding youtubePlayerView
    @BindView(R.id.yvPlayer)  YouTubePlayerView youTubePlayerView;

    private final static String YOUTUBE_API_KEY = "AIzaSyCnfTbICoDrNz_ESkwiF2caf1H7lbEh7fI";
    private String key;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_quick_play);
        ButterKnife.bind(this);

        // getting youtube key from intent
        key = getIntent().getStringExtra("key");

        // call youtube player
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener(){
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(key);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(QuickPlayActivity.this, "Could not play youtube video", Toast.LENGTH_LONG).show();
            }
        });
    }
}
