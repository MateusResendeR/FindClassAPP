package com.findclass.ajvm.findclassapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;

import java.util.List;

public class ProfessorRankingAdapter extends RecyclerView.Adapter<ProfessorRankingAdapter.MyViewHolder> {
    //Elementos auxiliares
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position ) {
        //Pegando dados da lista para os itens do adapter
        User professor = professors.get(position);
        int place = position+1;
        holder.name.setText(professor.getName());
        if(professor.getScore() < 0){
            holder.score.setText("Pontuação: 0");
        }else {
            holder.score.setText("Pontuação: "+professor.getScore());
        }
        //Definição de posição do professor
        holder.positionTextView.setText(place+"º lugar");
        //Atribuição de icone por posição
        if (place == 1){
            holder.icon.setImageResource(R.drawable.ic_1position);
        }else if (place == 2){
            holder.icon.setImageResource(R.drawable.ic_2position);
        }else if (place == 3){
            holder.icon.setImageResource(R.drawable.ic_3position);
        }
    }

    @Override
    public int getItemCount() {
        return professors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //Elementos gráficos
        private TextView name;
        private TextView score;
        private TextView positionTextView;
        private ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Setando elementos gráficos
            name = itemView.findViewById(R.id.textViewNomeProfessorRanking);
            score = itemView.findViewById(R.id.textViewScoreProfessorRanking);
            positionTextView = itemView.findViewById(R.id.textViewPosition);
            icon = itemView.findViewById(R.id.imageViewIconPosition);
        }
    }
}
