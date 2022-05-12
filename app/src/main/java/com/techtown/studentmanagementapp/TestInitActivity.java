package com.techtown.studentmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestInitActivity extends AppCompatActivity {
    static public String TAG = "TestInitActivity";

    private Button button_launch;
    private Button button_start;
    private Button button_main;
    private Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_init);

        button_launch = findViewById(R.id.button_launch);
        button_launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("Launch");
            }
        });
        button_start = findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("start");
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
        button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("login");
            }
        });
    }

    private void sendIntent(String activity) {
        if (activity.equalsIgnoreCase("Launch")) {
            Intent intent_launch = new Intent(getApplicationContext(), LaunchActivity.class);
            startActivity(intent_launch);
        }

        if (activity.equalsIgnoreCase("Start")) {
            Intent intent_Start = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent_Start);
        }

        if (activity.equalsIgnoreCase("Main")) {
            Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent_main);
        }

        if (activity.equalsIgnoreCase("login")) {
            Intent intent_login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent_login);
        }
    }
}