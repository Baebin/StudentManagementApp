package com.techtown.studentmanagementapp.manager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtown.studentmanagementapp.entity.Student;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FirebaseManager {
    static public String TAG = "FirebaseManager";

    static private String token = "";

    static private FirebaseDatabase fdb;
    static private DatabaseReference ref_students;
    static private DatabaseReference ref_users;

    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAjQPg08Q:APA91bGiPkcrD1ZiXMqJ-XgxAxnUWvhO5wvKutbCi4uan7Htk5XQQaVYo_9rhdq7PhjHAzzl-9GorH2s52_hF6N7PMSR_KuocvDTG402j0pBXS42K0vBb9LZb-MxXHkOZYfB6a8uW7eK";

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
            String gc_token = "";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                Log.d(TAG, "count: " + count);

                int i = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String gc_user = dataSnapshot.getValue().toString();
                    Log.d(TAG, "gc_user: " + gc_user);
                    if (gc.equals(gc_user)) {
                        gc_token = dataSnapshot.getKey();
                        tokens.add(gc_token);

                        Log.d(TAG, ++i + ". " + gc_token);
                    }
                }

                if (!gc_token.equals(""))
                    callStudents(gc, tokens);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "The read failed: " + error.getCode());
            }
        });
    }

    private static void callStudents(String gc, List<String> tokens) {
        Student student = StudentManager.getGCStudent(gc);

        String grade_ = student.getGrade_() + "";
        String class_ = student.getClass_() + "";

        Log.d(TAG, "callStudents(): " + tokens);
        Log.d(TAG, "gc: " + gc
                    + "\n" + "grade: " + grade_
                    + "\n" + "class: " + class_);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String token: tokens) {
                    try {
                        JSONObject root = new JSONObject();
                        JSONObject notification = new JSONObject();
                        notification.put("grade", grade_);
                        notification.put("class", class_);
                        root.put("notification", notification);
                        root.put("to", token);

                        URL Url = new URL(FCM_MESSAGE_URL);
                        HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setRequestProperty("Content-type", "application/json");

                        OutputStream os = connection.getOutputStream();
                        os.write(root.toString().getBytes("utf-8"));
                        os.flush();
                        connection.getResponseCode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
