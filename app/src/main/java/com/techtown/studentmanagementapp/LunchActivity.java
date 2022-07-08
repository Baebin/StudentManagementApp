package com.techtown.studentmanagementapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.techtown.studentmanagementapp.entity.Time;
import com.techtown.studentmanagementapp.manager.LunchParsingManager;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LunchActivity extends AppCompatActivity {
    static public String TAG = "LunchActivity";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private TextView tv_mon;
    private TextView tv_tues;
    private TextView tv_wed;
    private TextView tv_thur;
    private TextView tv_fri;

    private Time time;
    private LunchParsingManager manager;

    private static final Pattern PATTERN_BRACKET = Pattern.compile("\\([^\\(\\)]+\\)");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);
        Log.d(TAG, "onCreate()");

        // lunch_main.xml
        tv_mon = findViewById(R.id.tv_mon);
        tv_tues = findViewById(R.id.tv_tues);
        tv_wed = findViewById(R.id.tv_wed);
        tv_thur = findViewById(R.id.tv_thur);
        tv_fri = findViewById(R.id.tv_fri);

        try {
            time = new Time(getTime()).setCalendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        manager = new LunchParsingManager(time);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 1000);
    }

    private void init() {
        Log.d(TAG, "init()");

        try {
            String foods;
            int stack = 0;
            for (List<String> foodsList: manager.getFoods()) {
                Log.d(TAG, "stack: " + stack++);
                if (foodsList == null) continue;

                foods = getFoodsList(foodsList);

                switch (stack) {
                    case 1:
                        tv_mon.setText(foods + "");
                        break;
                    case 2:
                        tv_tues.setText(foods + "");
                        break;
                    case 3:
                        tv_wed.setText(foods + "");
                        break;
                    case 4:
                        tv_thur.setText(foods + "");
                        break;
                    case 5:
                        tv_fri.setText(foods + "");
                        break;
                    default:
                        return;
                }
            }
        } catch (JSONException | org.json.simple.parser.ParseException
                | ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        return dateFormat.format(date);
    }

    private String getFoodsList(List<String> foods) {
        String foodsList = foods + "";
        foodsList = foodsList
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "")
                .replace(",", ", ");

        Matcher matcher = PATTERN_BRACKET.matcher(foodsList);
        String remArea = "";

        while(matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            remArea = foodsList.substring(startIndex, endIndex);
            foodsList = foodsList.replace(remArea, "");
            matcher = PATTERN_BRACKET.matcher(foodsList);
        }

        return foodsList;
    }
}