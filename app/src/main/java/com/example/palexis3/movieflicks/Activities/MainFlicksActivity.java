package com.example.palexis3.movieflicks.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.palexis3.movieflicks.Fragments.UpcomingMovieFragment;
import com.example.palexis3.movieflicks.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFlicksActivity extends AppCompatActivity {

    //private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter recyclerAdapter;
    FragmentPagerAdapter adapterViewPager;

    // instantiating views
    @BindView(R.id.toolbar) Toolbar toolbar;
   // @BindView(R.id.tvThisWeek) TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_flicks);
        ButterKnife.bind(this);

        // setting the toolbar
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

     /*
        mRecyclerView = (RecyclerView) findViewById(R.id.rvMovies);

        // set up vertical linear layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // call movie caller async task
        fetchMovies();
      */
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_OF_PAGES = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_OF_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new UpcomingMovieFragment();
                case 1:
                    return new UpcomingMovieFragment();
                case 2:
                    return new UpcomingMovieFragment();
                default:
                    return null;
            }
        }
    }


    /*
    public void fetchMovies() {
        new MovieCallerTask().execute(",");
    }
    */

   /** TODO: Searching functionality, don't know how to populate this view once a search comes in */

  /*
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
  */

  /*
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

            ArrayList<NewMovies> movieList = new ArrayList<>();
            JSONArray movieJSONResults;

            try {
                JSONObject json = new JSONObject(response);
                movieJSONResults = json.getJSONArray("results");
                movieList.addAll(NewMovies.fromJSONArray(movieJSONResults));
            } catch(JSONException e) {
                Log.d("OKHTTP-DEBUG", e.toString());
                Toast.makeText(getApplicationContext(), "Error: Json parsing exception", Toast.LENGTH_LONG).show();
            }

            // add movie list to recycler view adapter
            recyclerAdapter = new UpcomingMovieRecyclerAdapter(getApplicationContext(), movieList);

            // set adapter to recycler view
            mRecyclerView.setAdapter(recyclerAdapter);
        }
    }
    */
}

