package com.techtown.studentmanagementapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.techtown.studentmanagementapp.entity.Time;
import com.techtown.studentmanagementapp.url.LunchParsingManager;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LunchActivity extends AppCompatActivity {
    static public String TAG = "LaunchActivity";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private TextView tv_day;
    private TextView tv_test;
    private Button button_test;

    private Time time;
    private LunchParsingManager manager;

    private int stack;
    private String[] days = { "월", "화", "수", "목", "금" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);




        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        applySetting();

        tv_day = findViewById(R.id.tv_day);
        tv_test = findViewById(R.id.tv_test);

        button_test = findViewById(R.id.button_test);
        button_test.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                tv_day.setText(time.getTime());

                addLine(time.getDays() + "");

                try {
                    if (manager.getFoods() != null) {
                        try {
                            stack = 0;
                            manager.getFoods().forEach(food -> pFood(food));
                        } catch (JSONException | org.json.simple.parser.ParseException
                                | ParseException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException | org.json.simple.parser.ParseException
                        | ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            time = new Time(getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        manager = new LunchParsingManager(time);
    }

    private void applySetting() {
        getSupportActionBar().setTitle("급식표");
    }

    private void pFood(List<String> food) {
        Log.d(TAG, "pFood(): " + food);

        if (stack > 4) stack = 0;
        addLine(days[stack++] + "요일: " + food);
    }

    private void addLine(String data) {
        Log.d(TAG, "addLine(): " + data);

        tv_test.append(data + "\n\n");
    }

    private String getTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        return dateFormat.format(date);
    }
}