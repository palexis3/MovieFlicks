package com.example.palexis3.movieflicks.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Adapters.MovieRecyclerAdapter;
import com.example.palexis3.movieflicks.Models.Movie;
import com.example.palexis3.movieflicks.Networking.MovieOkHttpClient;
import com.example.palexis3.movieflicks.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFlicksActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;

    // instantiating views
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flicks);
        ButterKnife.bind(this);

        // setting the toolbar
        setSupportActionBar(toolbar);

        //client = new MovieApiClient();
        // add movie list to recycler view adapter
        //recyclerAdapter = new MovieRecyclerAdapter(getApplicationContext(), movieList);
        //mRecyclerView.setAdapter(recyclerAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvMovies);

        // set up vertical linear layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // call movie caller async task
        fetchMovies();


        /** Old listview array adpater implementation
         *
         *
         @BindView(R.id.lvMovies) ListView lvItems;

        movieList = new ArrayList<>();

        //instantiating movie array adapter
        movieAdapter = new MovieArrayAdapter(this, movieList);

        //setting movie adapter
        lvItems.setAdapter(movieAdapter);

        client = new MovieApiClient();

        // listen if any items within the listview are clicked
        lvItemsListener();

        // fetch movies from api
        fetchMovies();
        */
    }

    public void fetchMovies() {
        new MovieCallerTask().execute("Task");
    }

    private class MovieCallerTask extends AsyncTask<String, Void, String> {

        // call the okhttp client and parse the movies into arraylist
        @Override
        protected String doInBackground(String... str) {

            String r = "Didn't succeed";

            // create an okHttp client to get JSON items
            MovieOkHttpClient client = new MovieOkHttpClient();

            // getting movies from api call
            try{
                r = client.getMovies();
                Log.d("OKHTTP-CLIENT", r);
                System.out.println(r);
            } catch(Exception e) {
                Log.d("DEBUG", e.getLocalizedMessage());
            }

            return r;
        }

        @Override
        protected void onPostExecute(String response) {

            if(response.equalsIgnoreCase("Didn't succeed")) {
                Toast.makeText(getApplicationContext(), "Could not get proper movie response in main thread", Toast.LENGTH_LONG).show();
            }

            ArrayList<Movie> movieList = new ArrayList<>();
            JSONArray movieJSONResults;

            try {
                JSONObject json = new JSONObject(response);
                movieJSONResults = json.getJSONArray("results");
                movieList.addAll(Movie.fromJSONArray(movieJSONResults));
            } catch(JSONException e) {
                Log.d("OKHTTP-DEBUG", e.toString());
                Toast.makeText(getApplicationContext(), "Error: Json parsing exception", Toast.LENGTH_LONG).show();
            }

            // add movie list to recycler view adapter
            recyclerAdapter = new MovieRecyclerAdapter(getApplicationContext(), movieList);

            mRecyclerView.setAdapter(recyclerAdapter);
        }
    }

    /*
    private void fetchMovies() {

        // making an asynchronous http request
        client.getMovies(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJSONResults;
                try {
                    //int curSize = recyclerAdapter.getItemCount();
                    movieJSONResults = response.getJSONArray("results");
                    movieList.addAll(Movie.fromJSONArray(movieJSONResults));
                    //recyclerAdapter.notifyItemRangeChanged(curSize, movieList.size());
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
    */

    // a method to listen if any particular items are clicked on
    /*
    private void lvItemsListener() {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                final Movie movie = movieList.get(position); // get the clicked item
                String id = String.valueOf(movie.getId());

                // asynchronous callback to get the youtube video for this specific movie
                client.lvItemClicked(id, new JsonHttpResponseHandler(){

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


                        // launch the movie details activity for unpopular movies
                        Intent i = new Intent(MainFlicksActivity.this, MovieDetailActivity.class);
                        i.putExtra("movie", Parcels.wrap(movie));
                        i.putExtra("key", key);
                        startActivity(i);

                        /** removed popular and average movie activity
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
     */
    }

