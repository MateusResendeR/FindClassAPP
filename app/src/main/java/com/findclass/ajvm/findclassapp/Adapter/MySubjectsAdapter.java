package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.R;

import java.util.List;

public class MySubjectsAdapter extends RecyclerView.Adapter<MySubjectsAdapter.MyViewHolder> {

    private List<Subject> mySubjects;
    private Context context;

    public MySubjectsAdapter(List<Subject> mySubjects, Context context) {
        this.mySubjects = mySubjects;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_my_subjects,
                parent,
                false
        );
        return new MyViewHolder(itemList);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Subject subject = mySubjects.get(position);

        holder.subjectName.setText(subject.getName());
        holder.subjectLevel.setText(subject.getLevel());

    }

    @Override
    public int getItemCount() {
        return mySubjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView subjectName;
        TextView subjectLevel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectNameTextView);
            subjectLevel = itemView.findViewById(R.id.subjectLevelTextView);
        }
    }
}
