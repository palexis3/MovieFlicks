package com.example.palexis3.movieflicks.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
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
    @BindView(R.id.tvThisWeek) TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_flicks);
        ButterKnife.bind(this);

        // setting the toolbar
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvMovies);

        // set up vertical linear layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // call movie caller async task
        fetchMovies();
    }

    public void fetchMovies() {
        new MovieCallerTask().execute(",");
    }

    // searching functionality
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // call async task for movie
                new MovieCallerTask().execute(query);

                // uppercase first letter
                query = query.substring(0, 1).toUpperCase() + query.substring(1);

                // set title
                tvTitle.setText(query);

                // reset search view
                searchView.clearFocus();
                searchView.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    // an asynctask class to get list of movie items in a background thread
    private class MovieCallerTask extends AsyncTask<String, Void, String> {

        // call the okhttp client and parse the movies into arraylist
        @Override
        protected String doInBackground(String... str) {

            String r = "Didn't succeed";

            // create an okHttp client to get JSON items
            MovieOkHttpClient client = new MovieOkHttpClient();

            // getting movies from api call or search query
            try{
                boolean search = !str[0].equalsIgnoreCase(","); // check if item is not a comma
                if(search) {
                    r = client.getSearchResponse(str[0]);
                } else {
                    r = client.getMovies();
                }
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

            // set adapter to recycler view
            mRecyclerView.setAdapter(recyclerAdapter);
        }
    }
}

