package com.example.palexis3.movieflicks.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Adapters.UpcomingMovieRecyclerAdapter;
import com.example.palexis3.movieflicks.Models.NewMovies;
import com.example.palexis3.movieflicks.Networking.MovieOkHttpClient;
import com.example.palexis3.movieflicks.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

// Using a recycler view as the layout view
public class UpcomingMovieFragment extends Fragment {

    private final static String TAG = "UpcomingMovieFragment";

    // instance variables
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private ArrayList<NewMovies> newMoviesArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    // inflate upcoming movie fragment xml
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_upcoming_movies, container, false);
        rootView.setTag(TAG);

        return rootView;
    }

    // bind all views
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(getActivity());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvMovies);

        // set up vertical linear layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        // call movie caller async task
        fetchMovies();
    }

    public void fetchMovies() {
        new MovieCallerTask().execute(",");
    }

    // an asynctask class to get list of movie items in a background thread
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
            } catch(Exception e) {
                Log.d("DEBUG", e.getLocalizedMessage());
            }

            return r;
        }

        @Override
        protected void onPostExecute(String response) {

            if(response.equalsIgnoreCase("Didn't succeed")) {
                Toast.makeText(getActivity(), "Could not get proper movie response in main thread", Toast.LENGTH_LONG).show();
            }

            newMoviesArrayList = new ArrayList<>();
            JSONArray movieJSONResults;

            try {
                JSONObject json = new JSONObject(response);
                movieJSONResults = json.getJSONArray("results");
                // adding all new movies from server call
                newMoviesArrayList.addAll(NewMovies.fromJSONArray(movieJSONResults));
            } catch(JSONException e) {
                Log.d("OKHTTP-DEBUG", e.toString());
                Toast.makeText(getActivity(), "Error: Json parsing exception", Toast.LENGTH_LONG).show();
            }

            // add movie list to recycler view adapter
            recyclerAdapter = new UpcomingMovieRecyclerAdapter(getActivity(), newMoviesArrayList);

            // set adapter to recycler view
            mRecyclerView.setAdapter(recyclerAdapter);

        }
    }
}
