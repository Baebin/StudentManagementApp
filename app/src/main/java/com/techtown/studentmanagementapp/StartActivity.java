package com.techtown.studentmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class StartActivity extends AppCompatActivity {
    static public String TAG = "StartActivity";
    private Button button_lunch;
    private Button button_start;
    private Button button_main;
    private Button button_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        button_lunch = findViewById(R.id.button_lunch);
        button_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        button_start = findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("Start");
            }
        });
        button_main = findViewById(R.id.button_main);
        button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("Main");
            }
        });
        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("Login");
            }
        });
    }
    private void sendIntent(String activity) {
        Log.d(TAG, "sendIntent(): " + activity);

        if (activity.equalsIgnoreCase("Lunch")) {
            Intent intent_lunch = new Intent(getApplicationContext(), LunchActivity.class);
            startActivity(intent_lunch);
        }

        if (activity.equalsIgnoreCase("Start")) {
            Intent intent_Start = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent_Start);
        }

        if (activity.equalsIgnoreCase("Main")) {
            Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent_main);
        }

        if (activity.equalsIgnoreCase("Login")) {
            Intent intent_login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent_login);
        }
    }
}


