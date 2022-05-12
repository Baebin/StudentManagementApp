package com.techtown.studentmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestInitActivity extends AppCompatActivity {
    static public String TAG = "TestInitActivity";

    private Button button_launch;

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
    }

    private void sendIntent(String activity) {
        if (activity.equalsIgnoreCase("Launch")) {
            Intent intent_launch = new Intent(getApplicationContext(), LaunchActivity.class);
            startActivity(intent_launch);
        }
    }
}