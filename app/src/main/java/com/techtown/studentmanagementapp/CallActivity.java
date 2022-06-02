package com.techtown.studentmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CallActivity extends AppCompatActivity {
    public static String TAG = "CallActivity";

    private int grade = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        Log.d(TAG, "onCreate()");

        Intent intent = getIntent();
        grade = intent.getIntExtra("grade", 1);

        Log.d(TAG, "grade: " + grade);
    }
}