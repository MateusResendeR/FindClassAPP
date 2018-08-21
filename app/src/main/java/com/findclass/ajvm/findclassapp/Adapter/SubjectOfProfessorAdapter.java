package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.findclass.ajvm.findclassapp.Model.Subject;
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
        Subject subject = subject_professor.getSubject();
        holder.subject.setText(subject.getName());
        holder.level.setText(subject.getLevel());
        if (subject.getName().equals("Matemática")){
            holder.icon.setImageResource(R.drawable.ic_math);
        }else if(subject.getName().equals("História")){
            holder.icon.setImageResource(R.drawable.ic_history);
        }else if(subject.getName().equals("Geografia")){
            holder.icon.setImageResource(R.drawable.ic_geo);
        }else if(subject.getName().equals("Português")){
            holder.icon.setImageResource(R.drawable.ic_menu_abc);
        }else if(subject.getName().equals("Inglês")){
            holder.icon.setImageResource(R.drawable.ic_eng);
        }else if(subject.getName().equals("Espanhol")){
            holder.icon.setImageResource(R.drawable.ic_spain);
        }else if(subject.getName().equals("Física")){
            holder.icon.setImageResource(R.drawable.ic_phys);
        }else if(subject.getName().equals("Química")){
            holder.icon.setImageResource(R.drawable.ic_chem);
        }else if(subject.getName().equals("Alemão")){
            holder.icon.setImageResource(R.drawable.ic_germany);
        }else if(subject.getName().equals("Francês")){
            holder.icon.setImageResource(R.drawable.ic_france);
        }else if(subject.getName().equals("Violão")){
            holder.icon.setImageResource(R.drawable.ic_menu_guitar);
        }else if(subject.getName().equals("Guitarra")){
            holder.icon.setImageResource(R.drawable.ic_guitar);
        }else if(subject.getName().equals("Flauta")){
            holder.icon.setImageResource(R.drawable.ic_flute);
        }else if(subject.getName().equals("Bateria")){
            holder.icon.setImageResource(R.drawable.ic_drum);
        }else if(subject.getName().equals("Canto")){
            holder.icon.setImageResource(R.drawable.ic_mic);
        }else if(subject.getName().equals("Italiano")){
            holder.icon.setImageResource(R.drawable.ic_italy);
        }

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView subject,level;
        private ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.textViewNameSubjectOfProfessor);
            level = itemView.findViewById(R.id.textViewLevelSubjectOfProfessor);
            icon = itemView.findViewById(R.id.imageViewIconSubjectOfProfessor);
        }
    }
}
