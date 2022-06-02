package com.techtown.studentmanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techtown.studentmanagementapp.entity.Student;
import com.techtown.studentmanagementapp.manager.FirebaseManager;
import com.techtown.studentmanagementapp.util.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity {
    static public String TAG = "MainActivity";

    public static Student student = null;
    public static String token = "";

    private FirebaseDatabase fdb;

    private Button button_1;
    private Button button_2;
    private Button button_3;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        SharedPreferenceUtil.init(this);
        student = SharedPreferenceUtil.getStudent();

        Log.d(TAG, "Student Updated");

        // Firebase
        fdb = FirebaseDatabase.getInstance();
        FirebaseManager.init(fdb);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isComplete()) {
                    token = task.getResult();
                    Log.d(TAG, "Token: " + token);

                    FirebaseManager.setToken(token);
                } else Log.d(TAG, "Token: Error");
            }
        });

        button_1 = findViewById(R.id.button_1);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("call_1");
            }
        });
        button_2 = findViewById(R.id.button_2);
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("call_2");
            }
        });
        button_3 = findViewById(R.id.button_3);
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("call_3");
            }
        });

    }

    public void sendIntent(String id) {
        Log.d(TAG, "sendIntent(" + id + ")");
        Intent intent_call = new Intent(MainActivity.this, CallActivity.class);

        switch (id) {
            case "call_1":
                intent_call.putExtra("grade", 1);
            case "call_2":
                intent_call.putExtra("grade", 2);
            case "call_3":
                intent_call.putExtra("grade", 3);
            default:
                break;
        }

        if (id.contains("call_")) startActivity(intent_call);
    }
}