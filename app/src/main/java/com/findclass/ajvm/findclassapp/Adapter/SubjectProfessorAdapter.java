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

public class SubjectProfessorAdapter extends RecyclerView.Adapter<SubjectProfessorAdapter.MyViewHolder> {
    private List<Subject_Professor> professors;
    private Context context;

    public SubjectProfessorAdapter(List<Subject_Professor> listProfessor,Context c) {
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
        Subject_Professor sp = professors.get(position);
        holder.nome.setText(sp.getUser().getName());
        holder.materia.setText(sp.getSubject().getName());
        holder.level.setText(sp.getSubject().getLevel());



    }

    @Override
    public int getItemCount() {
        return professors.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nome, materia, level;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textViewNomeProfessor);
            materia = itemView.findViewById(R.id.textViewMateriaProfessor);
            level = itemView.findViewById(R.id.textViewLevelProfessor);
        }
    }
}
