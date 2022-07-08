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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techtown.studentmanagementapp.manager.FirebaseManager;
import com.techtown.studentmanagementapp.manager.StudentManager;
import com.techtown.studentmanagementapp.util.SharedPreferenceUtil;

public class StartActivity extends AppCompatActivity {
    static public String TAG = "StartActivity";
    static public boolean develop_mode = false;

    private String token = "";

    private View view;

    private Button button_login_guest;
    private Button button_login;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.d(TAG, "onCreate()");

        // Hide Action Bar
        getSupportActionBar().hide();

        // SharedPreferences
        SharedPreferenceUtil.init(this);

        // Firebase
        FirebaseManager.init(FirebaseDatabase.getInstance());
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

        // activity_start.xml
        view = findViewById(R.id.view);

        button_login_guest = findViewById(R.id.button_login_guest);
        button_login = findViewById(R.id.button_login);

        button_login_guest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                sendIntent("login_guest");
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                sendIntent("login");
            }
        });

        // Auto Login Logic
        if (SharedPreferenceUtil.checkStudent()) {
            Log.d(TAG, "Auto Login");
            if (!develop_mode) {
                if (StudentManager.checkDeveloper(SharedPreferenceUtil.getStudent())) {
                    Log.d(TAG, "Developer Logined.");
                    showToast("개발자 계정이 활성화 되었습니다.");
                    develop_mode = true;
                    sendIntent("test_init");
                    return;
                }
            }
            sendIntent("main");
        } else {
            Log.d(TAG, "Auto Login Failed");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendIntent(String id) {
        Log.d(TAG, "sendIntent(" + id + ")");

        switch (id) {
            case "main":
                Intent intent_main = new Intent(StartActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_main);
                break;
            case "login":
                Intent intent_login = new Intent(StartActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                break;
            case "login_guest":
                if (token.equals("")) {
                    Log.d(TAG, "Token: Error");
                    showSnackbar("잠시 후 다시 시도해주세요.");
                    return;
                }

                FirebaseManager.removeStudent();
                SharedPreferenceUtil.putStudent(StudentManager.getGuest());

                Intent intent_login_guest = new Intent(StartActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
                startActivity(intent_login_guest);
                break;
            case "test_init":
                Intent test_init = new Intent(StartActivity.this, TestInitActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(test_init);
                break;
            default:
                break;
        }
    }

    private void showToast(String data) {
        Log.d(TAG, "showToast(" + data + ")");
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(String data) {
        Log.d(TAG, "showSnackbar(" + data + ")");
        final Snackbar snackbar = Snackbar.make(view, data, Snackbar.LENGTH_SHORT);
        snackbar.setAction("확인", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        }).show();
    }
}