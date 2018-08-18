package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;

import java.util.List;

public class ProfessorRankingAdapter extends RecyclerView.Adapter<ProfessorRankingAdapter.MyViewHolder> {
    private List<User> professors;
    private Context context;

    public ProfessorRankingAdapter(List<User> listProfessors, Context context) {
        this.professors = listProfessors;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_professor_ranking,parent,false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User professor = professors.get(position);
        holder.name.setText(professor.getName());
        if(professor.getScore() < 0){
            holder.score.setText("Pontuação: 0");
        }else {
            holder.score.setText("Pontuação: "+professor.getScore());
        }
    }

    @Override
    public int getItemCount() {
        return professors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView score;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewNomeProfessorRanking);
            score = itemView.findViewById(R.id.textViewScoreProfessorRanking);
        }
    }
}
