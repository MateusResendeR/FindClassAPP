package com.findclass.ajvm.findclassapp.menuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class SubjectOfProfessorActivity extends AppCompatActivity {
    //Elemnetos do firebase
    private DatabaseReference professorSubjectRef;
    private DatabaseReference subjectRef;
    //Elementos gráficos
    private TextView textViewNome;
    private RecyclerView recyclerViewSubjectsOfProfessor;
    //Elementos auxiliares
    private SubjectOfProfessorAdapter adapter;
    private ValueEventListener valueEventListenerSubjectProfessor;
    private ArrayList<Subject_Professor> listSubjectProfessor = new ArrayList<>();
    private User professor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_of_professor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setando atributos
        adapter = new SubjectOfProfessorAdapter(listSubjectProfessor,this);
        //Setando atributos gráficos
        recyclerViewSubjectsOfProfessor = findViewById(R.id.recyclerViewSubjectsOfProfessor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSubjectsOfProfessor.setLayoutManager(layoutManager);
        recyclerViewSubjectsOfProfessor.setHasFixedSize(true);
        recyclerViewSubjectsOfProfessor.setAdapter(adapter);
        textViewNome = findViewById(R.id.textViewNameProfessorOfSubject);
        //Recuperando dados da Activity passada
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            professor = (User) bundle.getSerializable("user");
            textViewNome.setText("Professor: "+professor.getName()+" "+professor.getSurname());
        }
        //setando atributos do firebase
        professorSubjectRef = FirebaseDatabase.getInstance().getReference().child("professorSubjects").child(professor.getId());
        subjectRef = FirebaseDatabase.getInstance().getReference().child("subjects");

        //Adição do evento de clique aos itens da lista
        recyclerViewSubjectsOfProfessor.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerViewSubjectsOfProfessor,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            //Definição da ação do clique.
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
        retriveSubjectOfProfessor();
    }
    //Método que define as ações que devem ser executadas ao abandonar a Activity
    @Override
    protected void onStop() {
        super.onStop();
        subjectRef.removeEventListener(valueEventListenerSubjectProfessor);
        professorSubjectRef.removeEventListener(valueEventListenerSubjectProfessor);
    }

    //Método que define a ação do botão voltar do hardware
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MenuAlunoActivity.class);
        startActivity(intent);
        finish();
    }

    //Método para buscar no banco de dados as disciplinas dos professores
    public void retriveSubjectOfProfessor(){
        listSubjectProfessor.clear();
        valueEventListenerSubjectProfessor = professorSubjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    final Professor_Subject professor_subject = data.getValue(Professor_Subject.class);
                    final Subject subject = new Subject();
                    retriveSubject(professor_subject,subject);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Método para buscar no banco de dados as disciplinas
    public void retriveSubject(final Professor_Subject professor_subject, final Subject subject){
        subjectRef.child(professor_subject.getSubjectId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subject.setSubject(dataSnapshot.getValue(Subject.class));
                Subject_Professor sp = new Subject_Professor(professor,subject,professor_subject);
                listSubjectProfessor.add(sp);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
