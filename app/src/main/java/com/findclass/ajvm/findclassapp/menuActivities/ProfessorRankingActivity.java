package com.findclass.ajvm.findclassapp.menuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    private RecyclerView recyclerViewProfessorRanking;
    private ProfessorRankingAdapter adapter;
    private ArrayList<User> listProfessors = new ArrayList<>();
    private DatabaseReference userRef;
    private ValueEventListener valueEventListenerProfessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_ranking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewProfessorRanking = findViewById(R.id.recyclerViewProfessorRanking);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");

        adapter = new ProfessorRankingAdapter(listProfessors,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewProfessorRanking.setLayoutManager(layoutManager);
        recyclerViewProfessorRanking.setHasFixedSize(true);
        recyclerViewProfessorRanking.setAdapter(adapter);

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

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        getProfessors();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerProfessor);
    }

    public void getProfessors(){
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
                Collections.sort(listProfessors);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
