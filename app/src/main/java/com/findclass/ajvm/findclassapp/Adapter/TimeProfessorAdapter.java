package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.Model.Time_Professor;
import com.findclass.ajvm.findclassapp.R;

import java.util.List;

public class TimeProfessorAdapter extends RecyclerView.Adapter<TimeProfessorAdapter.MyViewHolder> {
    private List<Time_Professor> professors;
    private Context context;

    public TimeProfessorAdapter(List<Time_Professor> listProfessor, Context c) {
        this.professors = listProfessor;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_professores,parent,false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Time_Professor sp = professors.get(position);
        holder.startTime.setText(sp.getUser().getName());
        holder.endTime.setText(sp.getTime().getStartTime());
        holder.day.setText(sp.getTime().getEndTime());



    }

    @Override
    public int getItemCount() {
        return professors.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView startTime, endTime, day;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.textViewNomeProfessor);
            endTime = itemView.findViewById(R.id.textViewMateriaProfessor);
            day = itemView.findViewById(R.id.textViewLevelProfessor);
        }
    }
}
