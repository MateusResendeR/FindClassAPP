package com.findclass.ajvm.findclassapp.menuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.findclass.ajvm.findclassapp.Adapter.SubjectOfProfessorAdapter;
import com.findclass.ajvm.findclassapp.Helper.RecyclerItemClickListener;
import com.findclass.ajvm.findclassapp.Model.Professor_Subject;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.Model.Subject_Professor;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SubjectOfProfessorActivity extends AppCompatActivity {
    private User professor;
    private TextView textViewNome;
    private DatabaseReference professorSubjectRef;
    private DatabaseReference subjectRef;
    private RecyclerView recyclerViewSubjectsOfProfessor;
    private SubjectOfProfessorAdapter adapter;
    private ArrayList<Subject_Professor> listSubjectProfessor = new ArrayList<>();
    private ValueEventListener valueEventListenerSubjectProfessor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_of_professor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewSubjectsOfProfessor = findViewById(R.id.recyclerViewSubjectsOfProfessor);


        adapter = new SubjectOfProfessorAdapter(listSubjectProfessor,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSubjectsOfProfessor.setLayoutManager(layoutManager);
        recyclerViewSubjectsOfProfessor.setHasFixedSize(true);
        recyclerViewSubjectsOfProfessor.setAdapter(adapter);

        textViewNome = findViewById(R.id.textViewNameProfessorOfSubject);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            professor = (User) bundle.getSerializable("user");
            textViewNome.setText("Professor: "+professor.getName()+" "+professor.getSurname());

        }
        professorSubjectRef = FirebaseDatabase.getInstance().getReference().child("professorSubjects").child(professor.getId());
        subjectRef = FirebaseDatabase.getInstance().getReference().child("subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerViewSubjectsOfProfessor.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerViewSubjectsOfProfessor,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getBaseContext(),
                                        AvailabilityListAlunoActivity.class);
                                Subject_Professor thisSubjectProfessor = listSubjectProfessor.get(position);
                                intent.putExtra("professor_uid",thisSubjectProfessor.getProfessorSubject().getProfessorUid());
                                intent.putExtra("subject_id",thisSubjectProfessor.getSubject().getId());

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
        getSubjectProfessor();
    }

    @Override
    protected void onStop() {
        super.onStop();
        subjectRef.removeEventListener(valueEventListenerSubjectProfessor);
        professorSubjectRef.removeEventListener(valueEventListenerSubjectProfessor);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MenuAlunoActivity.class);
        startActivity(intent);
        finish();
    }

    public void getSubjectProfessor(){
        listSubjectProfessor.clear();
        valueEventListenerSubjectProfessor = professorSubjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    final Professor_Subject ps = data.getValue(Professor_Subject.class);
                    final Subject subject = new Subject();
                    subjectRef.child(ps.getSubjectId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            subject.setSubject(dataSnapshot.getValue(Subject.class));
                            Subject_Professor sp = new Subject_Professor(professor,subject,ps);
                            listSubjectProfessor.add(sp);
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
