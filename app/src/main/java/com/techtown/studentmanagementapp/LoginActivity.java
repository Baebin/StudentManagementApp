package com.techtown.studentmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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

        view = findViewById(R.id.view);

        edtv_grade = findViewById(R.id.edtv_grade);
        edtv_class = findViewById(R.id.edtv_class);
        edtv_number = findViewById(R.id.edtv_number);
        edtv_name = findViewById(R.id.edtv_name);

        button_next = findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSnackbar("Test");
            }
        });
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