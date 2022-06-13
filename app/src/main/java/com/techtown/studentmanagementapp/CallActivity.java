package com.techtown.studentmanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
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

    private ValueEventListener listener = null;

    public static boolean click = true;

    private View view;

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
        view = findViewById(R.id.view);
        studentView = findViewById(R.id.student_layout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        studentView.setLayoutManager(linearLayoutManager);

        click = true;
        setAdapter();
        FirebaseManager.ref_classes.addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "The read failed: " + error.getCode());
            }
        });
    }

    private void initAdapter() {
        Log.d(TAG, "initAdapter()");
        studentsAdapter = new StudentsAdapter();
        studentsAdapter.setOnItemClickListener(new OnStudentsClickListener() {
            @Override
            public void onItemClickListener(StudentsAdapter.ViewHolder holder, View view, int position) {
                Log.d(TAG, "studentsAdapter.onItemClickListener() : " + position);

                if (!click) {
                    showSnackbar("잠시 후 다시 시도해주세요.");
                    return;
                }

                Student student = new Student(grade, position+1, 0, "");
                FirebaseManager.turnClasses(
                        StudentManager.getGC(student)
                );

                click = false;
                Log.d(TAG, "click: false");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        click = true;
                        Log.d(TAG, "click: true");
                    }
                }, 3000);
            }
        });
    }

    private void setAdapter() {
        Log.d(TAG, "setAdapter()");
        initAdapter();
        FirebaseManager.ref_classes.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CardView view;

                Map<String, Boolean> classes = new HashMap<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String gc_class = dataSnapshot.getKey();
                    classes.put(gc_class, true);
                }

                for (int i = 1; i <= 10; i++) {
                    String gc_class = grade + "";

                    if (i >= 10) gc_class += i;
                    else gc_class += "0" + i;

                    Student student = new Student(grade, i, 0, "");

                    if (classes.containsKey(gc_class)) {
                        // Green Background
                        studentsAdapter.add(student, true);
                        Log.d(TAG, "cardView(" + i +"): Green");
                    } else {
                        // Gray Background
                        studentsAdapter.add(student, false);
                        Log.d(TAG, "cardView(" + i +"): Gray");
                    }
                }
                studentView.setAdapter(studentsAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "The read failed: " + error.getCode());
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestory()");
        if (listener != null) FirebaseManager.ref_classes.removeEventListener(listener);
        super.onDestroy();
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