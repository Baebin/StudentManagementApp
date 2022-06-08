package com.techtown.studentmanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtown.studentmanagementapp.adapter.StudentsAdapter;
import com.techtown.studentmanagementapp.entity.Student;
import com.techtown.studentmanagementapp.listener.OnStudentsClickListener;
import com.techtown.studentmanagementapp.manager.FirebaseManager;
import com.techtown.studentmanagementapp.manager.StudentManager;

import java.util.HashMap;
import java.util.Map;

public class CallActivity extends AppCompatActivity {
    public static String TAG = "CallActivity";

    private int grade;

    private StudentsAdapter studentsAdapter;
    private RecyclerView studentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        Log.d(TAG, "onCreate()");

        // Firebase
        FirebaseManager.init(FirebaseDatabase.getInstance());

        // Intent
        grade = getIntent().getIntExtra("grade", 1);
        Log.d(TAG, "grade: " + grade);

        // activity_call.xml
        studentView = findViewById(R.id.student_layout);
        studentsAdapter = new StudentsAdapter();
        studentsAdapter.setOnItemClickListener(new OnStudentsClickListener() {
            @Override
            public void onItemClickListener(StudentsAdapter.ViewHolder holder, View view, int position) {
                Log.d(TAG, "studentsAdapter.onItemClickListener() : " + position);

                Student student = new Student(grade, position+1, 0, "");
                FirebaseManager.callStudents(
                        StudentManager.getGC(student)
                );
                FirebaseManager.turnClasses(
                        StudentManager.getGC(student)
                );
            }
        });

        for (int i = 1; i <= 10; i++){
            Log.d(TAG, "Student: " + i);
            Student student = new Student(grade, i, 0, "");
            studentsAdapter.addStudent(student);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        studentView.setLayoutManager(linearLayoutManager);

        studentView.setAdapter(studentsAdapter);

        FirebaseManager.ref_classes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Boolean> classes = new HashMap<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String gc_class = dataSnapshot.getKey();
                    classes.put(gc_class, true);
                }

                for (int i = 0; i < 10; i++) {
                    String gc_class = grade + "";

                    if (i >= 10) gc_class += i;
                    else gc_class += "0" + i;

                    if (classes.containsKey(gc_class)) {
                        // Green Background
                    } else {
                        // Gray Background
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "The read failed: " + error.getCode());
            }
        });
    }
}