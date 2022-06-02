package com.techtown.studentmanagementapp.manager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtown.studentmanagementapp.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager {
    static public String TAG = "FirebaseManager";

    static private String token = "";

    static private FirebaseDatabase fdb;
    static private DatabaseReference ref_students;
    static private DatabaseReference ref_users;

    public static void init(FirebaseDatabase fdb) {
        Log.d(TAG, "init()");

        FirebaseManager.fdb = fdb;
        ref_students = fdb.getReference("Students");
        ref_users = fdb.getReference("Users");
    }

    public static void setToken(String token) {
        Log.d(TAG, "setToken(): " + token);
        FirebaseManager.token = token;
    }

    public static void saveStudent(Student student) {
        String gc = StudentManager.getGC(student);
        Log.d(TAG, "saveStudent(): " + gc);

        ref_students.child(token).setValue(gc);
    }

    public static void callStudents(String gc) {
        Log.d(TAG, "callStudents(): " + gc);

        List<String> tokens = new ArrayList<>();
        ref_students.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String gc_user = dataSnapshot.getValue().toString();
                    if (gc.equals(gc_user)) {
                        String gc_token = dataSnapshot.getKey();
                        tokens.add(gc_token);

                        Log.d(TAG, count + ". " + gc_token);
                    }
                }

                callStudents(tokens);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "The read failed: " + error.getCode());
            }
        });
    }

    private static void callStudents(List<String> tokens) {
        Log.d(TAG, "callStudents(): " + tokens);
    }
}
