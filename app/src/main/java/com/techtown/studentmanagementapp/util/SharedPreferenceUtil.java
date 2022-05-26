package com.techtown.studentmanagementapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.techtown.studentmanagementapp.entity.Student;

public class SharedPreferenceUtil {
    public static String TAG = "SharedPreferenceUtil";
    public static SharedPreferences util;
    public static SharedPreferences.Editor editor;

    public static void init(Context context) {
        Log.d(TAG, "init()");

        util = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void putStudent(Student student) {
        if (util == null) {
            Log.d(TAG, "SharedPreferences Null Exception");
            return;
        }

        String profile = SerializeUtil.serialize(student);

        editor = util.edit();
        editor.putString("Student", profile);
        editor.commit();

        Log.d(TAG, "profile: " + profile);
    }
}
