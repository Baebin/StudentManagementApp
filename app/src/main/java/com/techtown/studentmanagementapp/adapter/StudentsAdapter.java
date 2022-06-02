package com.techtown.studentmanagementapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techtown.studentmanagementapp.R;
import com.techtown.studentmanagementapp.entity.Student;
import com.techtown.studentmanagementapp.listener.OnStudentsClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {
    private static final String TAG = "FriendsAdapter";

    List<Student> studentArrayList = new ArrayList<>();

    private OnStudentsClickListener studentsClickListener;

    public void setOnItemClickListener(OnStudentsClickListener listener) {
        this.studentsClickListener = listener;
    }

    public void addFriend(Student student) {
        studentArrayList.add(student);
    }

    public void setStudents(ArrayList<Student> studentArrayList) {
        this.studentArrayList = studentArrayList;
    }

    public void resetStudents() {
        studentArrayList = new ArrayList<Student>();
    }

    public Student getStudents(int position) {
        return studentArrayList.get(position);
    }

    public void setStudent(int position, Student student) {
        studentArrayList.set(position, student);
    }

    @NonNull
    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View studentView = inflater.inflate(R.layout.call_card_item, parent, false);

        return new ViewHolder(studentView, studentsClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsAdapter.ViewHolder holder, int position) {
        Student student = studentArrayList.get(position);
        holder.setFriend(student);
    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        CircleImageView iv_image;

        public ViewHolder(@NonNull View itemView, OnStudentsClickListener listener) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            iv_image = itemView.findViewById(R.id.iv_image);

            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        studentsClickListener.onItemClickListener(StudentsAdapter.ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setFriend(Student student) {
            tv_name.setText(student.getGrade_() + "학년 " + student.getClass_() + "반");
        }
    }
}
