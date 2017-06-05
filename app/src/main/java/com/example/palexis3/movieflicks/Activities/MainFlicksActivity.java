package com.example.palexis3.movieflicks.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Fragments.NearbyMovieFragment;
import com.example.palexis3.movieflicks.Fragments.PopularTvShowsFragment;
import com.example.palexis3.movieflicks.Fragments.UpcomingMovieFragment;
import com.example.palexis3.movieflicks.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFlicksActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final static int REQUEST_LOCATION = 1;
    private Location location;

    FragmentPagerAdapter adapterViewPager;
    private GoogleApiClient googleApiClient;


    // instantiating views
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_flicks);
        ButterKnife.bind(this);

        // setting the toolbar
        setSupportActionBar(toolbar);


        // create the google client to start receiving updates
        if(googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainFlicksActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(MainFlicksActivity.this)
                    .addOnConnectionFailedListener(MainFlicksActivity.this).build();
        }

        // get the Viewpager and set it's PagerAdapter so it can display it's items
        ViewPager viewPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        // give the tablayout the viewpager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onStart() {
        super.onStart();

        // connect the google api client
        googleApiClient.connect();
    }


    @Override
    public void onStop() {

        // Disconnecting the client invalidates it.
        //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        // only stop if it's connected or it'll crash
        if(googleApiClient != null) {
            // disconnect google api client
            googleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                // get latitude and longitude from Google Play services api
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if(location != null) {
                    String res = String.format("Lat: %s, Long: %s", location.getLatitude(), location.getLongitude());
                    Toast.makeText(this, res, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // checks the location permission
    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(MainFlicksActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Check Permissions Now (Async)
                // This will show the standard permission request dialog UI
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainFlicksActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    // process the permission result from the user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode) {

           case REQUEST_LOCATION: {

               // If request is cancelled, the result arrays are empty.
               if (grantResults.length > 0
                       && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   // permission was granted, yay! Do the
                   // location-related task you need to do.
                   if (ContextCompat.checkSelfPermission(this,
                           Manifest.permission. ACCESS_FINE_LOCATION)
                           == PackageManager.PERMISSION_GRANTED) {

                       // get latitude and longitude from Google Play services api
                       location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                       if(location != null) {
                           String res = String.format("Lat: %s, Long: %s", location.getLatitude(), location.getLongitude());
                           Toast.makeText(this, res, Toast.LENGTH_LONG).show();
                       }
                   }

               } else {
                   // permission denied! Cannot get the location
                   location = null;
                   Toast.makeText(this, "Cannot show nearby movies without location!", Toast.LENGTH_LONG).show();
               }

               return;
           }
       }
    }

    // create page adapter for each fragment
    public static class MyPagerAdapter extends FragmentPagerAdapter {

            private static int NUM_OF_PAGES = 3;
            private static String[] tabTitles = new String[]{"New Movies", "Tv Shows", "Nearby Movies"};


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
                        return new PopularTvShowsFragment();
                    case 2:
                        return new NearbyMovieFragment();
                    default:
                        return null;
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }
        }

        // callbacks given by the Google Play services API
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Toast.makeText(this, "Google api client failed to connect", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
        }

        @Override
        public void onConnectionSuspended(int i) {
        }


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
            MyOkHttpClient client = new MyOkHttpClient();

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

