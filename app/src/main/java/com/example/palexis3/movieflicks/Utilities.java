package com.example.palexis3.movieflicks;


import java.util.HashMap;
import java.util.List;

public class Utilities {

    // converts a list of strings to a single string
    public static String convertToString(List<String> list) {

        if(list == null || list.size() == 0) return "N/A";

        StringBuilder sb = new StringBuilder();
        sb.append(list.get(0));

        for(int i = 1; i < list.size(); i++) {
            sb.append(", ");
            sb.append(list.get(i));
        }

        return sb.toString();
    }


    // converting numerical date to string based date
    public static String parseDate(String inputDate) {

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

}
