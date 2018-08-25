package com.findclass.ajvm.findclassapp.menuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Model.ScheduleObject;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RatingProfessorActivity extends AppCompatActivity {
    //Elemenetos do firebase
    private DatabaseReference userRef;
    private DatabaseReference scheduleRef;
    //Elementos auxiliares
    private User professor;
    private User userP;
    private User userS;
    private ScheduleObject schedule;
    private ValueEventListener valueEventListenerP;
    private ValueEventListener valueEventListenerS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_professor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setando atributos do firebase
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        scheduleRef = FirebaseDatabase.getInstance().getReference().child("schedule");
        //Setando atributos gráficos
        TextView textViewNome = findViewById(R.id.textViewNameProfessorRating);
        TextView textViewLevel = findViewById(R.id.textViewLevelProfessorRating);
        TextView textViewSubject = findViewById(R.id.textViewSubjectProfessorRating);
        //Recuperando dados da Activity passada
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            professor = (User) bundle.getSerializable("user");
            Subject subject = (Subject) bundle.getSerializable("subject");
            schedule = (ScheduleObject)bundle.getSerializable("schedule");
            textViewNome.setText(professor.getName());
            textViewSubject.setText(subject.getName());
            textViewLevel.setText(subject.getLevel());
        }
    }

    //Método que define as ações que devem ser executadas ao iniciar a Activity
    @Override
    protected void onStart() {
        super.onStart();
        retriverProfessor();
        retriverStudent();
    }

    //Método que define as ações que devem ser executadas ao abandonar a Activity
    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerP);
        userRef.removeEventListener(valueEventListenerS);
    }

    //Método para buscar no banco de dados o professor da aula
    public void retriverProfessor(){
        valueEventListenerP = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(User.class).getId().equals(professor.getId())){
                        userP = data.getValue(User.class);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });
    }

    //Método para buscar no banco de dados o aluno da aula
    public void retriverStudent(){
        valueEventListenerS = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(User.class).getId().equals(schedule.getStudent().getId())){
                        userS = data.getValue(User.class);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });

    }

    //Método para avaliar o professor como muito bom
    public void veryGoodRating(View view){
        scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("rating").setValue("1");
        userRef.child(userP.getId()).child("score").setValue(userP.getScore()+20);
        Toast.makeText(this, "Professor avaliado com sucesso!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MenuAlunoActivity.class));
    }

    //Método para avaliar o professor como bom
    public void goodRating(View view){
        scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("rating").setValue("1");
        userRef.child(userP.getId()).child("score").setValue(userP.getScore()+10);
        Toast.makeText(this, "Professor avaliado com sucesso!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MenuAlunoActivity.class));
    }

    //Método para avaliar o professor como médio
    public void averageRating( View view){
        scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("rating").setValue("1");
        userRef.child(userP.getId()).child("score").setValue(userP.getScore()+1);
        Toast.makeText(this, "Professor avaliado com sucesso!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MenuAlunoActivity.class));
    }

    //Método para avaliar o professor como ruim
    public void badRating(View view){
        scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("rating").setValue("1");
        userRef.child(userP.getId()).child("score").setValue(userP.getScore()-2);
        Toast.makeText(this, "Professor avaliado com sucesso!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MenuAlunoActivity.class));
    }

}
