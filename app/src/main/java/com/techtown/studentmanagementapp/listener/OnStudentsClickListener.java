package com.techtown.studentmanagementapp.listener;

import android.view.View;

import com.techtown.studentmanagementapp.adapter.StudentsAdapter;

public interface OnStudentsClickListener {
    void onItemClickListener(StudentsAdapter.ViewHolder holder, View view, int position);
}
