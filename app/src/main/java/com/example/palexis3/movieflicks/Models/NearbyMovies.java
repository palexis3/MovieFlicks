package com.example.palexis3.movieflicks.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Parcel
public class NearbyMovies {

    String title;
    String releaseDate;
    List<String> genres;
    String description;
    List<String> cast;
    List<Showtimes> showtimesList;

    /** Getters */

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getCast() {
        return cast;
    }

    public List<Showtimes> getShowtimesList() {
        return showtimesList;
    }


    // empty constructor for parceler library
    public NearbyMovies(){};

    // default constructor
    public NearbyMovies(JSONObject jsonObject) throws JSONException {
        this.title = jsonObject.getString("title");
        this.releaseDate = parseDate(jsonObject.getString("releaseDate"));
        this.description = jsonObject.getString("longDescription");
        this.showtimesList = Showtimes.fromJSONArray(jsonObject.getJSONArray("showtimes"));
        this.genres = runLoop(jsonObject.getJSONArray("genres"));
        this.cast = runLoop(jsonObject.getJSONArray("topCast"));
    }

    // runLoop takes an json array that purely has strings as items and returns an arraylist of strings
    private static ArrayList<String> runLoop(JSONArray jsonArray) {
        ArrayList<String> results = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                results.add(jsonArray.getString(i));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }


    // converting numerical date to string based date
    private static String parseDate(String inputDate) {

        HashMap<String, String> map = new HashMap<>();

        map.put("1", "January");
        map.put("2", "February");
        map.put("3", "March");
        map.put("4", "April");
        map.put("5", "May");
        map.put("6", "June");
        map.put("7", "July");
        map.put("8", "August");
        map.put("9", "September");
        map.put("10", "October");
        map.put("11", "November");
        map.put("12", "December");


        String[] arr = inputDate.split("-");
        String month = arr[1].charAt(0) == '0' ? arr[1].substring(1) : arr[1];
        String res = String.format("%s %s, %s", map.get(month), arr[2], arr[0]);
        return res;
    }


    public static ArrayList<NearbyMovies> fromJSONArray(JSONArray jsonArray) {
        ArrayList<NearbyMovies> results = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                results.add(new NearbyMovies(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

}
