package com.techtown.studentmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class TestInitActivity extends AppCompatActivity {
    static public String TAG = "TestInitActivity";

    // Button
    private Button button_lunch;
    private Button button_start;
    private Button button_main;
    private Button button_login;
    private Button button_login_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_init);

        Log.d(TAG, "onCreate()");

        button_lunch = findViewById(R.id.button_lunch);
        button_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("Lunch");
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
        button_login_admin = findViewById(R.id.button_login_admin);
        button_login_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("AdminLogin");
            }
        });

        // Firebase
        /*
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:605655454660:android:b289cb85b1c2c8c66e8a91") // Required for Analytics.
                .setProjectId("studentmanagementapp-69d22") // Required for Firebase Installations.
                .setApiKey("AIzaSyDlb3GA92rbW_-8nVjtqOsq5tJosyaWEVs") // Required for Auth.
                .build();
        FirebaseApp.initializeApp(this, options, "StudentManagementApp");
        Log.d(TAG, "FirebaseApp.initializeApp()");
         */
    }

    private void sendIntent(String activity) {
        Log.d(TAG, "sendIntent(): " + activity);

        if (activity.equalsIgnoreCase("Lunch")) {
            Intent intent_lunch = new Intent(getApplicationContext(), LunchActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
            startActivity(intent_lunch);
        }

        if (activity.equalsIgnoreCase("Start")) {
            Intent intent_Start = new Intent(getApplicationContext(), StartActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
            startActivity(intent_Start);
        }

        if (activity.equalsIgnoreCase("Main")) {
            Intent intent_main = new Intent(getApplicationContext(), MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
            startActivity(intent_main);
        }

        if (activity.equalsIgnoreCase("Login")) {
            Intent intent_login = new Intent(getApplicationContext(), LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
            startActivity(intent_login);
        }

        if (activity.equalsIgnoreCase("AdminLogin")) {
            Intent intent_login_admin = new Intent(getApplicationContext(), LoginAdminActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
            startActivity(intent_login_admin);
        }
    }
}