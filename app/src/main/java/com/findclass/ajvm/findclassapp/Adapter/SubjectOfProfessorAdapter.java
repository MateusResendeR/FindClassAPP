package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.findclass.ajvm.findclassapp.Model.Subject_Professor;
import com.findclass.ajvm.findclassapp.R;

import java.util.List;

public class SubjectOfProfessorAdapter extends RecyclerView.Adapter<SubjectOfProfessorAdapter.MyViewHolder>{
    private List<Subject_Professor> subjects;
    private Context context;

    public SubjectOfProfessorAdapter(List<Subject_Professor> subjects,Context context) {
        this.subjects = subjects;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subject_of_professor,parent,false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Subject_Professor subject_professor = subjects.get(position);
        holder.subject.setText(subject_professor.getSubject().getName());
        holder.level.setText(subject_professor.getSubject().getLevel());

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView subject,level;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.textViewNameSubjectOfProfessor);
            level = itemView.findViewById(R.id.textViewLevelSubjectOfProfessor);
        }
    }
}
