package com.example.palexis3.movieflicks.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class TvShows {

    int id;
    double rating;
    String name;
    String overView;
    String posterPath;
    String backdropPath;

    // get these items after calling specific '/details' endpoint
    List<String> genres;
    List<String> networks;
    String homepage_url;
    String lastAirDate;
    String youtubeKey;
    int numOfEpisodes;
    int numOfSeasons;


    // getters
    public int getId() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getOverView() {
        return overView;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath);
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getNetworks() {
        return networks;
    }

    public String getHomepage_url() {
        return homepage_url;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public int getNumOfEpisodes() {
        return numOfEpisodes;
    }

    public int getNumOfSeasons() {
        return numOfSeasons;
    }

    // setters
    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public void setNetworks(ArrayList<String> networks) {
        this.networks = networks;
    }

    public void setHomepage_url(String homepage_url) {
        this.homepage_url = homepage_url;
    }

    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }

    public void setNumOfEpisodes(int numOfEpisodes) {
        this.numOfEpisodes = numOfEpisodes;
    }

    public void setNumOfSeasons(int numOfSeasons) {
        this.numOfSeasons = numOfSeasons;
    }

    // empty constructor for Parceler library
    public TvShows(){};


    public TvShows(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.rating = jsonObject.getDouble("vote_average");
        this.posterPath = jsonObject.getString("poster_path");
        this.name = jsonObject.getString("name");
        this.overView = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
    }

    // factory method to convert a json array to list of tvShow objects
    public static ArrayList<TvShows> fromJSONArray(JSONArray array) {
        ArrayList<TvShows> results = new ArrayList<>();

        // convert each element in the array to a TvShows object
        for(int i = 0; i < array.length(); i++) {
            try{
                results.add(new TvShows(array.getJSONObject(i)));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
