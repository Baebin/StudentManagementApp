package com.techtown.studentmanagementapp.entity;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Time {
    static public String TAG = "Time";

    private String year;
    private String month;
    private String day;

    private List<String> days = new ArrayList<>();

    private Date date = new Date();

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private Calendar calendar = Calendar.getInstance(Locale.KOREA);;

    // Constructor
    public Time(String data) throws ParseException {
        Log.d(TAG, "Time(" + format + ")");

        if (!data.contains("-")) return;
        String[] time = data.split("-");

        if (time.length == 3) {
            this.year = time[0];
            this.month = time[1];
            this.day = time[2];

            date = format.parse(year + month + day);
            calendar.setTime(date);

            setCalendar();
        }

        Log.d(TAG,
                 "year: " + time
                    + "month: " + month
                    + "day: " + day);
    }

    // Method
    public String getTime() {
        if (year != null && month != null && day != null) {
            return    year + "년 "
                    + month + "월 "
                    + day + "일";
        }

        return null;
    }

    public Time setCalendar() {
        for (int i = 2; i <= 8; i++) {
            calendar.add(Calendar.DATE, i - calendar.get(Calendar.DAY_OF_WEEK));
            days.add(format.format(calendar.getTime()));
            calendar.setTime(date);
        }
        return this;
    }

    // Getter & Setter
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public Date getDate() {
        return date;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
