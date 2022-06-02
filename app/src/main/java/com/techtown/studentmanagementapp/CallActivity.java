package com.techtown.studentmanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.techtown.studentmanagementapp.adapter.StudentsAdapter;
import com.techtown.studentmanagementapp.entity.Student;
import com.techtown.studentmanagementapp.listener.OnStudentsClickListener;

public class CallActivity extends AppCompatActivity {
    public static String TAG = "CallActivity";

    private StudentsAdapter studentsAdapter;
    private RecyclerView studentView;

    private int grade = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        Log.d(TAG, "onCreate()");

        Intent intent = getIntent();
        grade = intent.getIntExtra("grade", 1);
        Log.d(TAG, "grade: " + grade);

        studentView = findViewById(R.id.student_layout);
        studentsAdapter = new StudentsAdapter();
        studentsAdapter.setOnItemClickListener(new OnStudentsClickListener() {
            @Override
            public void onItemClickListener(StudentsAdapter.ViewHolder holder, View view, int position) {
                Log.d(TAG, "studentsAdapter.onItemClickListener() : " + position);
            }
        });

        for (int i = 1; i <= 10; i++){
            Student student = new Student(grade, i, 0, "");
            studentsAdapter.addFriend(student);
        }
        studentView.setAdapter(studentsAdapter);
    }
}