package com.techtown.studentmanagementapp.manager;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeManager {
    public DateTimeFormatter dateFormat;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TimeManager setFormat() {
        return setFormat("yyyyMMdd");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TimeManager setFormat(String format) {
        dateFormat = DateTimeFormatter.ofPattern(format);
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTime() {
        LocalDate now = LocalDate.now();
        return now.format(dateFormat);
    }
}