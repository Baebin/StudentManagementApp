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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techtown.studentmanagementapp.entity.Student;
import com.techtown.studentmanagementapp.manager.FirebaseManager;
import com.techtown.studentmanagementapp.util.SharedPreferenceUtil;

public class LoginActivity extends AppCompatActivity {
    static public String TAG = "LoginActivity";

    private View view;

    private EditText edtv_grade;
    private EditText edtv_class;
    private EditText edtv_number;
    private EditText edtv_name;

    private Button button_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate()");

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

        view = findViewById(R.id.view);

        edtv_grade = findViewById(R.id.edtv_grade);
        edtv_class = findViewById(R.id.edtv_class);
        edtv_number = findViewById(R.id.edtv_number);
        edtv_name = findViewById(R.id.edtv_name);

        button_next = findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

        SharedPreferenceUtil.init(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void checkLogin() {
        String output = " 정보가 입력되지 않았습니다.";
        String error = " 정보가 올바르지 않습니다.";

        String grade_ = edtv_grade.getText().toString();
        String class_ = edtv_class.getText().toString();
        String number_ = edtv_number.getText().toString();
        String name_ = edtv_name.getText().toString();

        Log.d(TAG, "grade: " + grade_
                        + "\nclass: " + class_
                        + "\nnumber: " + number_
                        + "\nname: " + name_);

        if (grade_.equals("")) { showSnackbar("학년" + output); return; }
        if (!grade_.equals("1") && !grade_.equals("2") && !grade_.equals("3")) { showSnackbar("학년" + error); return; }
        if (!grade_.chars().allMatch(Character::isDigit)) { showSnackbar("학년" + error); }

        if (class_.equals("")) { showSnackbar("반" + output); return; }
        if (!class_.chars().allMatch(Character::isDigit)) { showSnackbar("반" + error); }
        int class_i;
        try {
            class_i = Integer.parseInt(class_);
        } catch (NumberFormatException e) {
            showSnackbar("반" + error);
            return;
        }
        if (class_i <= 0 || class_i >= 10) { showSnackbar("반" + error); return; }

        if (number_.equals("")) { showSnackbar("번호" + output); return; }
        if (!number_.chars().allMatch(Character::isDigit)) { showSnackbar("번호" + error); }
        int number_i;
        try {
            number_i = Integer.parseInt(number_);
        } catch (NumberFormatException e) {
            showSnackbar("반" + error);
            return;
        }
        if (number_i <= 0) { showSnackbar("번호: " + error); return; }

        if (name_.equals("")) { showSnackbar("이름" + output); return; }
        if (name_.length() < 2) { showSnackbar("이름" + error); return; }

        Student student = new Student(grade_, class_, number_, name_);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SharedPreferenceUtil.putStudent(student);
        }
        showSnackbar(student.getName_() + "님, 환영합니다.");

        FirebaseManager.saveStudent(student);
        sendIntent();
    }

    public void sendIntent() {
        Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent_main);
    }

    public void showToast(String data) {
        Log.d(TAG, "showToast(" + data + ")");
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    public void showSnackbar(String data) {
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