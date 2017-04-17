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

    private final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=f1e55cc01616b64d1b66566ca00d707a";

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
                Movie movie = movieList.get(position); // get the clicked item

                // launch youtube client since this is a popular movie
                if(movie.getRating() > 5) {
                    Toast.makeText(MainFlicksActivity.this, "Just clicked a popular movie", Toast.LENGTH_SHORT).show();
                } else {
                    // launch the movie details activity for unpopular movies
                    Intent i = new Intent(MainFlicksActivity.this, MovieDetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    startActivity(i);
                }
            }
        });
    }

}
