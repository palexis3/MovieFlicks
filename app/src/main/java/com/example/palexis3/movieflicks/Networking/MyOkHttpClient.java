package com.example.palexis3.movieflicks.Networking;

import android.util.Log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyOkHttpClient {

    // used for logging (debugging)
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

    // create only one instance of okHttp client for performance
    private static okhttp3.OkHttpClient client;

    /** Upcoming movies and popular tv shows implementation leverages the api.theMoviedb.org endpoints */

    private final static String REGION = "US";
    private final static String LANGUAGE = "en-US";
    private final static String API_KEY = "f1e55cc01616b64d1b66566ca00d707a";
    private final static String POPULAR_TV_SHOW_URL = "https://api.themoviedb.org/3/tv/on_the_air";
    private final static String TV_SHOW_DETAILS_URL = "https://api.themoviedb.org/3/tv/%s";
    private final static String UPCOMING_MOVIE_URL = "https://api.themoviedb.org/3/movie/upcoming";
    private final static String UPCOMING_MOVIE_VIDEO_URL = "https://api.themoviedb.org/3/movie/%s/videos";
    private final static String SEARCH_MOVIE_URL = "https://api.themoviedb.org/3/search/movie";

    /** Nearby Movies implementation leverages the data.tmsapi.com endpoints */

    private final static String TMS_API_KEY = "afygcfx8hkh6mskbu5qrgp53";
    private final static String NEARBY_MOVIES_URL = "http://data.tmsapi.com/v1.1/movies/showings";

    public MyOkHttpClient() {
        this.client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    // get upcoming movies synchronously from movies api
    public String getMovies() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(UPCOMING_MOVIE_URL).newBuilder();
        urlBuilder.addQueryParameter("api_key", API_KEY);
        urlBuilder.addQueryParameter("region", REGION);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()) {
            Log.d("DEBUG", "okHttp client issues!");
            throw new IOException("ERROR: OkHttp request call");
        }
        return response.body().string();
    }

    // get the youtube key for a clicked movie
    public String getSingleMovie(String movieId) throws IOException {
        String temp_url = String.format(UPCOMING_MOVIE_VIDEO_URL, movieId);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(temp_url).newBuilder();
        urlBuilder.addQueryParameter("api_key", API_KEY);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()) {
            Log.d("DEBUG", "okHttp single movie key issue!");
            throw new IOException("Error: okHttp single key request call");
        }
        return response.body().string();
    }

    // get the search response from a query
    public String getSearchResponse(String query) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(SEARCH_MOVIE_URL).newBuilder();
        urlBuilder.addQueryParameter("api_key", API_KEY);
        urlBuilder.addQueryParameter("query", query);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()) {
            Log.d("DEBUG", "okHttp search query issue!");
            throw new IOException("Error: okHttp search query issue!");
        }
        return response.body().string();
    }

    // get popular tv shows
    public String getPopularShows() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(POPULAR_TV_SHOW_URL).newBuilder();
        urlBuilder.addQueryParameter("api_key", API_KEY);
        urlBuilder.addQueryParameter("language", LANGUAGE);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()) {
            Log.d("POPULAR MOVIE", "okHttp popular movie issue!");
            throw new IOException("Error: okHttp popular movie issue!");
        }
        return response.body().string();
    }

    // get details about a specific tv show
    public String getShowDetails(String id) throws IOException {
        String temp_url = String.format(TV_SHOW_DETAILS_URL, id);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(temp_url).newBuilder();
        urlBuilder.addQueryParameter("api_key", API_KEY);
        urlBuilder.addQueryParameter("language", LANGUAGE);
        urlBuilder.addQueryParameter("append_to_response", "videos"); // get youtube videos
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()) {
            Log.d("SHOW DETAILS", "okHttp tv show details issue!");
            throw new IOException("Error: tv show details issue!");
        }
        return response.body().string();
    }

    // get nearby movies for a user
    public String getNearbyMovies(String latitude, String longitude) throws IOException {

        // get the current date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String current_date = dateFormat.format(date).toString();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(NEARBY_MOVIES_URL).newBuilder();
        urlBuilder.addQueryParameter("startDate", current_date);
        urlBuilder.addQueryParameter("api_key", TMS_API_KEY);
        urlBuilder.addQueryParameter("lat", latitude);
        urlBuilder.addQueryParameter("lng", longitude);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()) {
            Log.d("NEARBY MOVIES", "okHttp nearby movies issue!");
            throw new IOException("Error: nearby movies request issue!");
        }

        return response.body().string();
    }

}
