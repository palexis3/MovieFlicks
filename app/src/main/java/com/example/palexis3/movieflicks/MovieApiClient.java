package com.example.palexis3.movieflicks;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MovieApiClient {

    private AsyncHttpClient client;
    private final static String API_KEY = "f1e55cc01616b64d1b66566ca00d707a";
    private final static String url = "https://api.themoviedb.org/3/movie/now_playing";
    private final static String video_url = "https://api.themoviedb.org/3/movie/%s/videos";

    public MovieApiClient() {
        this.client =  new AsyncHttpClient();
    }

    // fetch movies from api asynchronously
    public void getMovies(JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.add("api_key", API_KEY);
        client.get(url, params, handler);
    }

    // get the youtube video for a specific movie
    public void lvItemClicked(String movieId, JsonHttpResponseHandler handler) {
        String temp_url = String.format(video_url, movieId);
        RequestParams params = new RequestParams();
        params.add("api_key", API_KEY);
        client.get(temp_url, params, handler);
    }

}
