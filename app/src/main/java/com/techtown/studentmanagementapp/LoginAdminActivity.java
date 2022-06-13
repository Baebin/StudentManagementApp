package com.techtown.studentmanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtown.studentmanagementapp.entity.AdminInfo;
import com.techtown.studentmanagementapp.manager.FirebaseManager;
import com.techtown.studentmanagementapp.util.SharedPreferenceUtil;

public class LoginAdminActivity extends AppCompatActivity {
    static public String TAG = "LoginAdminActivity";

    private View view;

    private EditText edtv_id;
    private EditText edtv_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        SharedPreferenceUtil.init(this);

        // Firebase
        FirebaseManager.init(FirebaseDatabase.getInstance());

        // activity_login_admin.xml
        view = findViewById(R.id.view);
    }

    private void checkLogin() {
        String output = " 정보가 입력되지 않았습니다.";

        String id = edtv_id.getText().toString();
        String pw = edtv_pw.getText().toString();

        Log.d(TAG, "id: " + id
                + "\npw: " + pw);

        if (id.equals("")) { showSnackbar("아이디" + output); return; }
        if (pw.equals("")) { showSnackbar("패스워드" + output); return; }

        FirebaseManager.ref_users.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String user_id = dataSnapshot.getKey();
                    Log.d(TAG, "user_id: " + user_id);
                    if (user_id.equals(id)) {
                        String user_pw = dataSnapshot.getValue().toString();
                        Log.d(TAG, "user_pw: " + user_pw);
                        if (user_pw.equals(pw)) {
                            Log.d(TAG, "Login Completed.");

                            SharedPreferenceUtil.putAdminInfo(
                                    new AdminInfo(id, pw)
                            );
                            sendIntent();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "The read failed: " + error.getCode());
            }
        });
    }

    private void sendIntent() {
        Intent intent_main = new Intent(LoginAdminActivity.this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
        startActivity(intent_main);
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