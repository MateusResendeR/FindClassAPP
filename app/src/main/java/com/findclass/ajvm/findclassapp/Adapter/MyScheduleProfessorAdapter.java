package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.Model.ScheduleObject;
import com.findclass.ajvm.findclassapp.R;

import java.util.List;

public class MyScheduleProfessorAdapter extends RecyclerView.Adapter<MyScheduleProfessorAdapter.MyViewHolder> {
    private List<ScheduleObject> mySchedules;
    private Context context;

    public MyScheduleProfessorAdapter(List<ScheduleObject> mySchedules, Context c) {
        this.mySchedules = mySchedules;
        this.context = c;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_schedules_professor,parent,false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ScheduleObject schedule = mySchedules.get(position);
        holder.subjectName.setText(schedule.getSubject().getName());
        holder.subjectLevel.setText(schedule.getSubject().getLevel());
        holder.studentName.setText(schedule.getStudent().getName());
    }

    @Override
    public int getItemCount() {
        return mySchedules.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, subjectLevel, studentName, date, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectNameTextView);
            subjectLevel = itemView.findViewById(R.id.subjectLevelTextView);
            studentName = itemView.findViewById(R.id.studentNameTextView);
            date = itemView.findViewById(R.id.dateTextView);
            time = itemView.findViewById(R.id.timeTextView);
        }
    }
}
