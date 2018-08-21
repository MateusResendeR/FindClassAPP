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
        return mySubjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView subjectName;
        TextView subjectLevel;
        private ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectNameTextView);
            subjectLevel = itemView.findViewById(R.id.subjectLevelTextView);
            icon = itemView.findViewById(R.id.imageViewIconMySubjects);
        }
    }
}
