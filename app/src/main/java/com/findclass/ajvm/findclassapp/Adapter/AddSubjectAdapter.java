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

public class AddSubjectAdapter extends RecyclerView.Adapter<AddSubjectAdapter.MyViewHolder> {

    private List<Subject> subjects;
    private Context context;

    public AddSubjectAdapter(List<Subject> subjects, Context context) {
        this.subjects = subjects;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_add_subject,
                parent,
                false
        );
        return new MyViewHolder(itemList);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.subjectName.setText(subject.getName());
        holder.subjectLevel.setText(subject.getLevel());
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView subjectName;
        TextView subjectLevel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectNameTextView);
            subjectLevel = itemView.findViewById(R.id.subjectLevelTextView);
        }
    }
}
