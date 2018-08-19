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
    private RecyclerView recyclerViewSubjectOfLevel;
    private SubjectOfLevelAdapter adapter;
    private String nameLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_of_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String[] subjects;

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            nameLevel = (String) bundle.getSerializable("level");
        }
        if(nameLevel.equals("Variados")){
            subjects= new String[]{"Inglês", "Espanhol", "Alemão", "Italiano", "Francês", "Violão", "Guitarra", "Flauta", "Bateria", "Canto"};
        }else {
            subjects= new String[]{"Matemática", "História", "Geografia", "Português", "Inglês", "Espanhol", "Física", "Química"};
        }
        recyclerViewSubjectOfLevel = findViewById(R.id.recyclerViewSubjectsOfLevel);

        adapter = new SubjectOfLevelAdapter(subjects,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSubjectOfLevel.setLayoutManager(layoutManager);
        recyclerViewSubjectOfLevel.setHasFixedSize(true);
        recyclerViewSubjectOfLevel.setAdapter(adapter);

        recyclerViewSubjectOfLevel.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerViewSubjectOfLevel,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getBaseContext(),SubjectCategoryLevelActivity.class);
                                intent.putExtra("subject",subjects[position]);
                                intent.putExtra("level",nameLevel);
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

}
