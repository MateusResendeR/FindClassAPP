package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.R;

import java.util.List;

import static com.findclass.ajvm.findclassapp.R.drawable.ic_flute;

public class SubjectOfLevelAdapter extends RecyclerView.Adapter<SubjectOfLevelAdapter.MyViewHolder> {
    private String[] subjects;
    private Context context;

    public SubjectOfLevelAdapter(String[] subjects, Context context) {
        this.subjects = subjects;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subject_of_level,parent,false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String subject = subjects[position];
        holder.name.setText(subject);
        if (subject.equals("Matemática")){
            holder.icon.setImageResource(R.drawable.ic_math);
        }else if(subject.equals("História")){
            holder.icon.setImageResource(R.drawable.ic_history);
        }else if(subject.equals("Geografia")){
            holder.icon.setImageResource(R.drawable.ic_geo);
        }else if(subject.equals("Português")){
            holder.icon.setImageResource(R.drawable.ic_menu_abc);
        }else if(subject.equals("Inglês")){
            holder.icon.setImageResource(R.drawable.ic_eng);
        }else if(subject.equals("Espanhol")){
            holder.icon.setImageResource(R.drawable.ic_spain);
        }else if(subject.equals("Física")){
            holder.icon.setImageResource(R.drawable.ic_phys);
        }else if(subject.equals("Química")){
            holder.icon.setImageResource(R.drawable.ic_chem);
        }else if(subject.equals("Alemão")){
            holder.icon.setImageResource(R.drawable.ic_germany);
        }else if(subject.equals("Francês")){
            holder.icon.setImageResource(R.drawable.ic_france);
        }else if(subject.equals("Violão")){
            holder.icon.setImageResource(R.drawable.ic_menu_guitar);
        }else if(subject.equals("Guitarra")){
            holder.icon.setImageResource(R.drawable.ic_guitar);
        }else if(subject.equals("Flauta")){
            holder.icon.setImageResource(R.drawable.ic_flute);
        }else if(subject.equals("Bateria")){
            holder.icon.setImageResource(R.drawable.ic_drum);
        }else if(subject.equals("Canto")){
            holder.icon.setImageResource(R.drawable.ic_mic);
        }else if(subject.equals("Italiano")){
            holder.icon.setImageResource(R.drawable.ic_italy);
        }



    }

    @Override
    public int getItemCount() {
        return subjects.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewNameSubjectOfLevel);
            icon = itemView.findViewById(R.id.imageViewIconSubject);
        }
    }
}
