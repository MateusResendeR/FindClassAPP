package com.findclass.ajvm.findclassapp.menuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.findclass.ajvm.findclassapp.Adapter.SubjectOfLevelAdapter;
import com.findclass.ajvm.findclassapp.Helper.RecyclerItemClickListener;
import com.findclass.ajvm.findclassapp.R;

public class SubjectsOfLevelActivity extends AppCompatActivity {
    //Elementos gráficos
    private RecyclerView recyclerViewSubjectOfLevel;
    //Elementos auxiliares
    private SubjectOfLevelAdapter adapter;
    private String nameLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_of_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Array que irá receber as disciplinas do seu respectivo level.
        final String[] subjects;
        //Recuperando dados da Activity passada
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            nameLevel = (String) bundle.getSerializable("level");
        }
        //Preenchendo array com as disciplinas do seu respectivo level.
        if(nameLevel.equals("Variados")){
            subjects= new String[]{"Inglês", "Espanhol", "Alemão", "Italiano", "Francês", "Violão", "Guitarra", "Flauta", "Bateria", "Canto"};
        }else {
            subjects= new String[]{"Matemática", "História", "Geografia", "Português", "Inglês", "Espanhol", "Física", "Química"};
        }
        //Setando atributos
        adapter = new SubjectOfLevelAdapter(subjects,this);
        //Setando atributos gráficos
        recyclerViewSubjectOfLevel = findViewById(R.id.recyclerViewSubjectsOfLevel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSubjectOfLevel.setLayoutManager(layoutManager);
        recyclerViewSubjectOfLevel.setHasFixedSize(true);
        recyclerViewSubjectOfLevel.setAdapter(adapter);
        //Adição do evento de clique aos itens da lista
        recyclerViewSubjectOfLevel.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerViewSubjectOfLevel,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            //Definição da ação do clique.
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getBaseContext(),SubjectCategoryLevelActivity.class);
                                intent.putExtra("subject",subjects[position]);
                                intent.putExtra("level",nameLevel);
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

}
