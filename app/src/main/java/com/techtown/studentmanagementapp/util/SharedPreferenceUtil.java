package com.techtown.studentmanagementapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.techtown.studentmanagementapp.entity.Student;
import com.techtown.studentmanagementapp.manager.StudentManager;

public class SharedPreferenceUtil {
    public static String TAG = "SharedPreferenceUtil";
    public static SharedPreferences util;
    public static SharedPreferences.Editor editor;

    public static void init(Context context) {
        Log.d(TAG, "init()");

        util = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static boolean checkUtil() {
        if (util == null) {
            Log.d(TAG, "getStudent(): SharedPreferences Null Exception");
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void putStudent(Student student) {
        Log.d(TAG, "putStudent()");
        if (!checkUtil()) return;

        String profile = SerializeUtil.serialize(student);

        editor = util.edit();
        editor.putString("Student", profile);
        editor.commit();

        Log.d(TAG, "profile: " + profile);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public Student getStudent() {
        Log.d(TAG, "getStudent()");
        if (!checkUtil()) return null;

        String profile = util.getString("Student", "");
        if (profile.equals("")) {
            Log.d(TAG, "getStudent(): Profile Null Exception");
            return StudentManager.guest;
        }
        Log.d(TAG, "profile: " + profile);

        return (Student) SerializeUtil.deserialize(profile);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public boolean checkStudent() {
        Log.d(TAG, "checkStudent()");
        if (!checkUtil()) return false;

        if (util.getString("Student", "").equals("")) return false;
        return true;
    }

    static public void removeStudent() {
        Log.d(TAG, "removeStudent()");
        if (!checkUtil()) return;

        editor = util.edit();
        editor.remove("Student");
        editor.commit();
    }
}