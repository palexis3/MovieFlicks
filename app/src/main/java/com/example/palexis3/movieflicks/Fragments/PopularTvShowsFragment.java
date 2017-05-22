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

import com.example.palexis3.movieflicks.Adapters.PopularTvShowsRecyclerAdapter;
import com.example.palexis3.movieflicks.Models.TvShows;
import com.example.palexis3.movieflicks.Networking.MyOkHttpClient;
import com.example.palexis3.movieflicks.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

// using recycler view as layout
public class PopularTvShowsFragment extends Fragment{

    private final static String TAG = "PopularTvShowsFragment";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private ArrayList<TvShows> tvShowsArrayList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // inflate the xml for this fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_tvshows, container, false);
        view.setTag(TAG);

        return view;
    }

    // bind all views
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.rvShows);

        // set up vertical layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        // call shows caller async task
        fetchShows();
    }

    public void fetchShows() { new TvShowCallerTask().execute("Shows"); }

    // this async task class gets popular shows in a background thread
    private class TvShowCallerTask extends AsyncTask<String, Void, String> {

        // call the okHttp client and parse shows into an arraylist
        @Override
        protected String doInBackground(String... params) {

            String r = "Didn't succeed";

            // create an okHttp client to get JSON items
            MyOkHttpClient client = new MyOkHttpClient();

            try{
                r = client.getPopularShows();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return r;
        }

        @Override
        protected void onPostExecute(String response) {

            if(response.equalsIgnoreCase("Didn't succeed")) {
                Toast.makeText(getActivity(), "Could not get TV shows response in async task", Toast.LENGTH_LONG).show();
            }

            tvShowsArrayList = new ArrayList<>();
            JSONArray tvShowJsonResults;

            try {
                JSONObject jsonObject = new JSONObject(response);
                tvShowJsonResults = jsonObject.getJSONArray("results");

                // add all tv shows from server call
                tvShowsArrayList.addAll(TvShows.fromJSONArray(tvShowJsonResults));
            } catch (JSONException e) {
                Log.d("OKHTTP-ERROR", e.toString());
                Toast.makeText(getActivity(), "Error: JSON PARSING EXCEPTION IN POPULAR MOVIES ASYNC TASK", Toast.LENGTH_LONG).show();
            }

            // add tv shows arraylist to recycler adapter
            recyclerAdapter = new PopularTvShowsRecyclerAdapter(getActivity(), tvShowsArrayList);

            // set the adapter for the recyclerview
            recyclerView.setAdapter(recyclerAdapter);
        }
    }
}
