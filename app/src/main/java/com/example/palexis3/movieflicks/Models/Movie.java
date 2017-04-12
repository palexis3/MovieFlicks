package com.example.palexis3.movieflicks.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {

    String posterPath;
    String originalTitle;
    String overView;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overView = jsonObject.getString("overview");
    }

    // method takes in a json array to then create a list of movie objects
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
