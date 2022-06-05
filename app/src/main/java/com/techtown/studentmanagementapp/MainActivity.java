package com.techtown.studentmanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techtown.studentmanagementapp.entity.Student;
import com.techtown.studentmanagementapp.manager.FirebaseManager;
import com.techtown.studentmanagementapp.manager.StudentManager;
import com.techtown.studentmanagementapp.manager.TimeManager;
import com.techtown.studentmanagementapp.util.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity {
    static public String TAG = "MainActivity";

    public static Student student = null;

    private FirebaseDatabase fdb;

    private DrawerLayout drawerLayout;
    private View view_drawer;

    private ImageView iv_profile;
    private Button button_lunch;
    private Button button_school;
    private Button button_calendar;
    private Button button_logout;

    private View view;
    private View main_layout;

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
        FirebaseManager.init(FirebaseDatabase.getInstance());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isComplete()) {
                    String token = task.getResult();
                    Log.d(TAG, "Token: " + token);

                    FirebaseManager.setToken(token);
                } else Log.d(TAG, "Token: Error");
            }
        });

        // activity_drawer_main.xml
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(drawerListener);

        view_drawer = findViewById(R.id.drawer_view);
        view_drawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        iv_profile = findViewById(R.id.iv_profile);
        iv_profile.setImageResource(R.drawable.human);

        button_lunch = findViewById(R.id.button_lunch);
        button_school = findViewById(R.id.button_school);
        button_calendar = findViewById(R.id.button_calendar);
        button_logout = findViewById(R.id.button_logout);

        button_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("lunch");
            }
        });
        button_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("school");
            }
        });
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("calendar");
            }
        });
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("start");
            }
        });

        // activity_main.xml
        view = findViewById(R.id.view);
        main_layout = findViewById(R.id.main_layout);

        button_1 = findViewById(R.id.button_1);
        button_2 = findViewById(R.id.button_2);
        button_3 = findViewById(R.id.button_3);

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("call_1");
            }
        });
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("call_2");
            }
        });
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent("call_3");
            }
        });

    }

    private DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            Log.d(TAG, "onDrawerSlide()");
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            Log.d(TAG, "onDrawerOpened()");
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            Log.d(TAG, "onDrawerClosed");
            main_layout.setEnabled(true);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            Log.d(TAG, "onDrawerStateChanged");
            main_layout.setEnabled(false);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendIntent(String id) {
        Log.d(TAG, "sendIntent(" + id + ")");
        Intent intent = null;

        if (id.contains("call_")) {
            intent = new Intent(MainActivity.this, CallActivity.class);
        }

        switch (id) {
            case "call_1":
                intent.putExtra("grade", 1);
                break;
            case "call_2":
                intent.putExtra("grade", 2);
                break;
            case "call_3":
                intent.putExtra("grade", 3);
                break;
            case "lunch":
                intent = new Intent(MainActivity.this, LunchActivity.class);
                break;
            case "school":
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        getString(R.string.school)
                ));
                break;
            case "calendar":
                String uri = getString(R.string.calendar)
                        + new TimeManager().setFormat().getTime();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        uri
                ));

                Log.d(TAG, "uri: " + uri);
                break;
            case "start":
                logout();
                intent = new Intent(MainActivity.this, StartActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void logout() {
        Log.d(TAG, "logout()");

        FirebaseManager.removeStudent();
        SharedPreferenceUtil.removeStudent();
    }

    @Override
    public void onBackPressed() {
        // Disable Back Button
        //super.onBackPressed();
    }
}