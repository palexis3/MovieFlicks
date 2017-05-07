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

}
