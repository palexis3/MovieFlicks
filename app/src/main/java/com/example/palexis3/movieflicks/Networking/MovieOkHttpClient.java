package com.example.palexis3.movieflicks.Networking;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieOkHttpClient {

    private OkHttpClient client;
    private final static String API_KEY = "f1e55cc01616b64d1b66566ca00d707a";
    private final static String URL = "https://api.themoviedb.org/3/movie/now_playing";
    private final static String VIDEO_URL = "https://api.themoviedb.org/3/movie/%s/videos";
    private final static String SEARCH = "https://api.themoviedb.org/3/search/movie";

    public MovieOkHttpClient() {
        this.client = new OkHttpClient();
    }

    // get movies synchronously from movies api
    public String getMovies() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
        urlBuilder.addQueryParameter("api_key", API_KEY);
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
        String temp_url = String.format(VIDEO_URL, movieId);
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(SEARCH).newBuilder();
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

}
