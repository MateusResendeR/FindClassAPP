package com.findclass.ajvm.findclassapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.Model.ScheduleObject;
import com.findclass.ajvm.findclassapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyScheduleProfessorAdapter extends RecyclerView.Adapter<MyScheduleProfessorAdapter.MyViewHolder> {
    private List<ScheduleObject> mySchedules;

    public MyScheduleProfessorAdapter(List<ScheduleObject> mySchedules) {
        this.mySchedules = mySchedules;
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

        String dateString = schedule.getDate().getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        holder.subjectName.setText(schedule.getSubject().getName());
        holder.subjectLevel.setText(schedule.getSubject().getLevel());
        holder.studentName.setText(schedule.getStudent().getName());
        holder.date.setText(dateFormat.format(date)+" ("+schedule.getTime().getDay()+")");
        holder.time.setText(schedule.getTime().getStartTime());
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
