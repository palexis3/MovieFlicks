package com.example.palexis3.movieflicks.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Parcel
public class NearbyMovies {

    private static final String API_KEY = "afygcfx8hkh6mskbu5qrgp53";

    String title;
    String imagePath;
    String url;
    String releaseDate;
    String movieRating;
    List<String> genres;
    String description;
    List<String> cast;
    List<String> advisories;
    List<Showtimes> showtimesList;

    /** Getters */

    public String getTitle() {
        return title;
    }

    public String getUrl() { return url; }

    /** TODO: Must figure out a way to get a movie's image from tms api, if that is too tedious, see if you can get image from querying by name to theMovieDB api*/
    public String getImagePath() {
        String res = "N/A";
        if(!imagePath.equalsIgnoreCase("N/A")) {
            res = String.format("http://developer.tmsimg.com/%s?api_key=%s", imagePath, API_KEY);
        }
        return res;
    }

    public String getMovieRating() { return movieRating; }

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

    public List<String> getAdvisories() { return advisories; }

    public List<Showtimes> getShowtimesList() {
        return showtimesList;
    }


    // empty constructor for parceler library
    public NearbyMovies(){};

    // default constructor
    public NearbyMovies(JSONObject jsonObject) throws JSONException {

        this.imagePath = (jsonObject.has("preferredImage")) ? jsonObject.getJSONObject("preferredImage").getString("uri") : "N/A";
        this.movieRating = (jsonObject.has("ratings")) ? jsonObject.getJSONArray("ratings").getJSONObject(0).getString("code") : "N/A";
        this.url = (jsonObject.has("officialUrl")) ? jsonObject.getString("officialUrl") : "N/A";
        this.title = jsonObject.getString("title");
        this.releaseDate = (jsonObject.has("releaseDate")) ? parseDate(jsonObject.getString("releaseDate")) : "N/A";
        if(jsonObject.has("longDescription")) {
            this.description = jsonObject.getString("longDescription");
        } else if(jsonObject.has("shortDescription")) {
            this.description = jsonObject.getString("shortDescription");
        } else {
            this.description = "N/A";
        }
        this.showtimesList = Showtimes.fromJSONArray(jsonObject.getJSONArray("showtimes"));
        this.genres = runLoop(jsonObject.getJSONArray("genres"));
        this.cast = (jsonObject.has("topCast")) ? runLoop(jsonObject.getJSONArray("topCast")) : new ArrayList<>(Arrays.asList("N/A"));
        this.advisories = (jsonObject.has("advisories")) ? runLoop(jsonObject.getJSONArray("advisories")) : new ArrayList<>(Arrays.asList("N/A"));
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

        if(inputDate == null || inputDate.length() == 0) return "N/A";

        String[] arr = inputDate.split("-");
        if(arr.length != 3) return "N/A";

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
