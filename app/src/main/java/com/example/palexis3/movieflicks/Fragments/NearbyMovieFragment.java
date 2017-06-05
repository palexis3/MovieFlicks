package com.example.palexis3.movieflicks.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Adapters.NearbyMoviesRecyclerAdapter;
import com.example.palexis3.movieflicks.Models.NearbyMovies;
import com.example.palexis3.movieflicks.Networking.MyOkHttpClient;
import com.example.palexis3.movieflicks.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.ButterKnife;

// implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener

public class NearbyMovieFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final static String TAG = "NearbyMovieFragment";
    private final static int REQUEST_LOCATION = 1;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private ArrayList<NearbyMovies> nearbyMoviesArrayList;
    private GoogleApiClient googleApiClient;
    private Location location;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create the google client to start receiving updates
        if(googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }
    }

    // inflate fragment xml
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby_movie, container, false);
        view.setTag(TAG);

        return view;
    }

    // bind views within fragment xml
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.rvNearbyMovies);

        // set up vertical linear manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        /*
        // check location permissions
        checkPermissions();

        // call nearby movie caller task
        fetchNearbyMovies();
        */
    }


   // callback given by the Google Play services API
   @Override
   public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
       Toast.makeText(getActivity(), "Google api client failed to connect", Toast.LENGTH_LONG).show();
       googleApiClient.disconnect();
   }

   // callback given by the Google Play services API
   @Override
   public void onConnectionSuspended(int i) {
   }

    @Override
    public void onStart() {
        super.onStart();

        // connect the google api client
        googleApiClient.connect();

        // check location permissions
        checkPermissions();

        // call nearby movie caller task
        fetchNearbyMovies();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, getActivity());

        // only stop if it's connected or it'll crash
        if(googleApiClient != null) {
            // disconnect google api client
            googleApiClient.disconnect();
        }

        super.onStop();
    }

    // this method checks the location permissions given by the Google play services
    // and updates the fragment's location object
    public void checkPermissions() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            // permission has been granted, continue as usual
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
    }


    // callback given by the Google Play services API
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // must check permissions first
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now (Async)
            // This will show the standard permission request dialog UI
           requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            // get latitude and longitude from Google Play services api
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            /*
            if (location != null) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
            } else {
                Toast.makeText(getActivity(), "Error: Could not get your current location!", Toast.LENGTH_LONG).show();
            }
            */
        }
    }

    // callback for request permissions
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission. ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                // We can now safely use the API we requested access to
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            } else {
                // Permission was denied or request was cancelled
                Log.d("LOCATION", "Wasn't able to get current location");
            }
        }
    }


    // call our nearby movie caller async task
    //public void fetchNearbyMovies(String latitude, String longitude) { new NearbyMovieCallerTask().execute(new String[]{latitude, longitude}); }

    public void fetchNearbyMovies() { new NearbyMovieCallerTask().execute(); }

    private class NearbyMovieCallerTask extends AsyncTask<Void, Void, String> {

        /** TODO: TRY TO SEE IF PLACING LOCATION APP PERMISSIONS CODE IN ASYNC TASAK WILL STOP THE APP FROM CRASHING */

        @Override
        protected String doInBackground(Void... params) {

            String r = "Didn't succeed";

            // create an okHttp client
            MyOkHttpClient client = new MyOkHttpClient();

            // get my latitude and longitude
            //String latitude = params[0][0];
            //String longitude = params[0][1];

            // hardcode the latitude and longitude
            //String latitude = "37.786073", longitude = "-122.41155";
            //String latitude = "26.1664736", longitude = "-80.2083806";

            String latitude = String.valueOf(location.getLatitude()), longitude = String.valueOf(location.getLongitude());

            try {
                r = client.getNearbyMovies(latitude, longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return r;
        }

        @Override
        protected void onPostExecute(String response) {

            if(response.equalsIgnoreCase("Didn't succeed")) {
                Toast.makeText(getActivity(), "Could not get nearby movies response in async task", Toast.LENGTH_LONG).show();
            }

            nearbyMoviesArrayList = new ArrayList<>();
            JSONArray jsonArray;

            try{
                jsonArray = new JSONArray(response);
                // add all nearby movies from api call
                nearbyMoviesArrayList.addAll(NearbyMovies.fromJSONArray(jsonArray));

            } catch (JSONException e) {
                Log.d("OKHTTP-ERROR", e.toString());
                Toast.makeText(getActivity(), "Cannot get nearby movies at this time!", Toast.LENGTH_LONG).show();
            }

            // add nearby movies to recycler adapter
            recyclerAdapter = new NearbyMoviesRecyclerAdapter(getActivity(), nearbyMoviesArrayList);

            // set the adapter for the recyclerview
            recyclerView.setAdapter(recyclerAdapter);
        }
    }
}
