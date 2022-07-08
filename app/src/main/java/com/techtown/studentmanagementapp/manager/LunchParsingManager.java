package com.techtown.studentmanagementapp.manager;

import android.util.Log;

import com.techtown.studentmanagementapp.entity.Time;
import com.techtown.studentmanagementapp.url.LunchParsingURL;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LunchParsingManager {
    static public String TAG = "LunchParsingManager";

    private LunchParsingURL url_A = null;
    private LunchParsingURL url_B = null;

    private Time time;

    public LunchParsingManager(Time time) {
        this.time = time;
        init();
    }

    private void init() {
        Log.d(TAG, "init()");

        // year & day: ex, 202205
        String yd = "";
        boolean stack = false;

        List<String> days_A = new ArrayList<>();
        List<String> days_B = new ArrayList<>();

        List<String> days = time.getDays();
        days.remove(6);
        days.remove(5);

        for (String day: days) {
            Log.d(TAG, day);

            if (yd.equals("")) yd = day.substring(0, 5);
            if (day.contains(yd)) {
                if (!stack) {
                    days_A.add(day);
                } else {
                    days_B.add(day);
                }
                continue;
            }

            yd = day.substring(0, 5);
            days_B.add(day);

            stack = true;
        }

        String first_A = days_A.get(0);
        String end_A = days_A.get(days_A.size()-1);
        url_A = new LunchParsingURL(first_A.substring(0, 5), first_A, end_A);

        try {
            url_A.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (stack) {
            String first_B = days_A.get(0);
            String end_B = days_A.get(days_A.size()-1);
            url_B = new LunchParsingURL(first_B.substring(0, 5), first_B, end_B);
        }
    }

    public List<List<String>> getFoods() throws JSONException, ParseException, java.text.ParseException, IOException {
        Log.d(TAG, "getFoods()");

        if (url_A == null) return null;

        Map<Integer, List<String>> meal_A = url_A.getResult();
        if (meal_A.isEmpty()) return null;

        List<List<String>> foods = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (!meal_A.containsKey(i)) continue;
            foods.add(meal_A.get(i));

            Log.d(TAG, meal_A.get(i) + "");
        }

        if (url_B != null) {
            Map<Integer, List<String>> meal_B = url_B.getResult();
            if (!meal_B.isEmpty()) {
                for (int i = 0; i < 5; i++) {
                    if (!meal_B.containsKey(i)) continue;
                    foods.add(meal_B.get(i));
                }
            }
        }

        return foods;
    }

    public LunchParsingURL getUrl_A() {
        return url_A;
    }
    public LunchParsingURL getUrl_B() {
        return url_B;
    }
}
