package com.example.palexis3.movieflicks.Models;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Showtimes {

    String theaterName;
    String date;
    String time;
    List<String> specs;
    String ticketUri;

    public Showtimes(JSONObject jsonObject) throws JSONException{
        this.theaterName = jsonObject.getJSONObject("theatre").getString("name");
        this.ticketUri = jsonObject.getString("ticketURI");
        String temp_specs = jsonObject.getString("quals");
        specs = Arrays.asList(temp_specs.split("|"));
    }
}
