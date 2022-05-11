package com.techtown.studentmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LaunchActivity extends AppCompatActivity {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    private String getTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        return dateFormat.format(date);
    }
}