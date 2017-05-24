package com.example.palexis3.movieflicks.Models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *  Showtimes is a model used for the NearbyMovies class b/c a specific movie may have many showtimes.
 */

@Parcel
public class Showtimes {

    String theaterName;
    String date;
    String time;
    String quals; // used for special specs about showtime, such as if 'AM'
    List<String> specs;
    String ticketUri;

    /** Getters */
    public List<String> getSpecs() {
        return specs;
    }

    public String getTicketUri() {
        return ticketUri;
    }


    public String getTheaterName() {
        return theaterName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }


    // empty constructor for parceler library
    public Showtimes(){};

    // constructor
    public Showtimes(JSONObject jsonObject) throws JSONException{

        // this check is if a showtime has an 'AM' time
        if(jsonObject.has("quals")) {
            this.quals = jsonObject.getString("quals");
            // getting the specs for a specific showtime
            String temp_specs = jsonObject.getString("quals");
            this.specs = new ArrayList<>(Arrays.asList(temp_specs.split("|")));
        } else {
            this.quals = "N/A";
            this.specs = new ArrayList<>(Arrays.asList("N/A"));
        }

        this.theaterName = jsonObject.getJSONObject("theatre").getString("name");
        this.ticketUri = (jsonObject.has("ticketURI")) ? jsonObject.getString("ticketURI") : "N/A";


        // getting date and times for a showtimes
        String[] dateTimeArr = jsonObject.getString("dateTime").split("T");
        this.date = parseDate(dateTimeArr[0]);
        this.time = parseTime(dateTimeArr[1], quals);
    }

    // convert time into more time friendly time
    private static String parseTime(String inputTime, String quals) {

        if(inputTime == null || inputTime.length() == 0) return "N/A";

        String timeOfDay = "PM";
        if(quals != null && quals.equalsIgnoreCase("A.M.")) {
            timeOfDay = "AM";
        }

        String[] arr = inputTime.split(":");
        int hours = Integer.parseInt(arr[0]);
        int minutes = Integer.parseInt(arr[1]);

        // checking the time
        if(hours > 12) {
            hours -= 12;
        }

        String res = String.format("%d:%d %s", hours, minutes, timeOfDay);
        return res;
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

    // factory method to convert json array of showtimes to a list of showtime objects
    public static ArrayList<Showtimes> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Showtimes> results = new ArrayList<>();

        // don't want to process to much showtimes!
        int defaultLength = 10;
        if(jsonArray.length() < 10) {
            defaultLength = jsonArray.length();
        }

        // convert each element in json array to a json object
        for(int i = 0; i < defaultLength; i++) {
            try{
                results.add(new Showtimes(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
