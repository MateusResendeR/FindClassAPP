package com.findclass.ajvm.findclassapp.menuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.findclass.ajvm.findclassapp.Adapter.ProfessorRankingAdapter;
import com.findclass.ajvm.findclassapp.Helper.RecyclerItemClickListener;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ProfessorRankingActivity extends AppCompatActivity {
    //Elemenetos do firebase
    private DatabaseReference userRef;
    //Elementos auxiliares
    private ProfessorRankingAdapter adapter;
    private ArrayList<User> listProfessors = new ArrayList<>();
    private ValueEventListener valueEventListenerProfessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_ranking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Setando atributos
        adapter = new ProfessorRankingAdapter(listProfessors,this);
        //setando atributos do firebase
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        //Setando atributos gráficos
        RecyclerView recyclerViewProfessorRanking = findViewById(R.id.recyclerViewProfessorRanking);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewProfessorRanking.setLayoutManager(layoutManager);
        recyclerViewProfessorRanking.setHasFixedSize(true);
        recyclerViewProfessorRanking.setAdapter(adapter);
        //Adição do evento de clique aos itens da lista
        recyclerViewProfessorRanking.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerViewProfessorRanking,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getBaseContext(),SubjectOfProfessorActivity.class);
                                intent.putExtra("user", listProfessors.get(position));
                                startActivity(intent);
                            }
                            @Override
                            public void onLongItemClick(View view, int position) {
                                //
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                //
                            }
                        }
                )
        );
    }

    //Método que define as ações que devem ser executadas ao iniciar a Activity
    @Override
    protected void onStart() {
        super.onStart();
        retriverProfessors();
    }

    //Método que define as ações que devem ser executadas ao abandonar a Activity
    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerProfessor);
    }

    //Método para buscar no banco de dados todos os professores
    public void retriverProfessors(){
        listProfessors.clear();
        valueEventListenerProfessor = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    if (user.getProfessor().equals("true")){
                        listProfessors.add(user);
                    }
                }
                //Ordenando lista de professores pelo ranking
                Collections.sort(listProfessors);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });
    }

}
