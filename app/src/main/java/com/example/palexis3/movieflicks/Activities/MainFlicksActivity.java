package com.example.palexis3.movieflicks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Adapters.MovieArrayAdapter;
import com.example.palexis3.movieflicks.Models.Movie;
import com.example.palexis3.movieflicks.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainFlicksActivity extends AppCompatActivity {

    private final static String API_KEY = "f1e55cc01616b64d1b66566ca00d707a";
    private final String url = String.format("https://api.themoviedb.org/3/movie/now_playing?api_key=%s", API_KEY);

    ArrayList<Movie> movieList;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flicks);

        // instantiating list view
        lvItems = (ListView) findViewById(R.id.lvMovies);

        movieList = new ArrayList<>();

        //instantiating movie array adapter
        movieAdapter = new MovieArrayAdapter(this, movieList);

        //setting movie adapter
        lvItems.setAdapter(movieAdapter);

        // listen if any items within the listview are clicked
        lvItemsListener();

        AsyncHttpClient client = new AsyncHttpClient();

        // making an asynchronous http request
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJSONResults;
                try {
                    movieJSONResults = response.getJSONArray("results");
                    movieList.addAll(Movie.fromJSONArray(movieJSONResults));
                    movieAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", movieList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(MainFlicksActivity.this, "Could not successfully make an http request to Movie DB!", Toast.LENGTH_LONG).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    // a method to listen if any particular items are clicked on
    private void lvItemsListener() {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                final Movie movie = movieList.get(position); // get the clicked item

                String video_url = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s", movie.getId(), API_KEY);

                // get trailer key for specific movie
                AsyncHttpClient client = new AsyncHttpClient();


                client.get(video_url, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        String key = null;

                        try{
                            // getting the key for youtube player for a movie
                            key = response.getJSONArray("results").getJSONObject(0).getString("key");
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        if(key == null) {
                            Toast.makeText(MainFlicksActivity.this, "Error: Could not get key for YouTube player", Toast.LENGTH_LONG).show();
                            throw new RuntimeException("Error: Could not get key for YouTube player");
                        }

                        // launch youtube client since this is a popular movie
                        if(movie.getRating() > 5) {
                            Intent i = new Intent(MainFlicksActivity.this, QuickPlayActivity.class);
                            i.putExtra("key", key);
                            i.putExtra("id", movie.getId());
                            startActivity(i);

                        } else {
                            // launch the movie details activity for unpopular movies
                            Intent i = new Intent(MainFlicksActivity.this, MovieDetailActivity.class);
                            i.putExtra("movie", Parcels.wrap(movie));
                            i.putExtra("key", key);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(MainFlicksActivity.this, "Could not get youtube key for specific movie", Toast.LENGTH_LONG).show();
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }
        });
    }

}
