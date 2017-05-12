package com.example.palexis3.movieflicks.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Movie {

    String youtubeKey;
    String posterPath;
    String originalTitle;
    String overView;
    String backdropPath;
    String releaseDate;
    double rating;
    int id;

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverView() {
        return overView;
    }

    public double getRating() {
        return rating;
    }

    public int getId() { return id; }

    public String getReleaseDate() { return releaseDate; }

    /// empty constructor needed by the Parceler library
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overView = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.rating = jsonObject.getDouble("vote_average");
        this.id = jsonObject.getInt("id");
        this.releaseDate = jsonObject.getString("release_date");
    }

    // factory method takes in a json array to then create a list of movie objects
    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        // convert each element in the json array to a movie object
        for(int i = 0; i < array.length(); i++) {
            try{
                results.add(new Movie(array.getJSONObject(i)));
            } catch(JSONException e) {
                e.printStackTrace();
            }

        }

        return results;
    }
}
