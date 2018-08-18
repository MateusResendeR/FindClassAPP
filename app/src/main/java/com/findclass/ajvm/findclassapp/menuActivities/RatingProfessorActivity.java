package com.findclass.ajvm.findclassapp.menuActivities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    private TextView textViewNome;
    private TextView textViewLevel;
    private TextView textViewSubject;
    private User professor;
    private Subject subject;
    private DatabaseReference userRef;
    private DatabaseReference scheduleRef;
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
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        scheduleRef = FirebaseDatabase.getInstance().getReference().child("schedule");

        //configuracao

        textViewNome = findViewById(R.id.textViewNameProfessorRating);
        textViewLevel = findViewById(R.id.textViewLevelProfessorRating);
        textViewSubject = findViewById(R.id.textViewSubjectProfessorRating);

        //recuperar dados
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            professor = (User) bundle.getSerializable("user");
            subject = (Subject) bundle.getSerializable("subject");
            schedule = (ScheduleObject)bundle.getSerializable("schedule");
            textViewNome.setText(professor.getName());
            textViewSubject.setText(subject.getName());
            textViewLevel.setText(subject.getLevel());
        }




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getProfessor();
        getStudent();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerP);
        userRef.removeEventListener(valueEventListenerS);
    }

    public void getProfessor(){
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

            }
        });

    }

    public void getStudent(){

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

            }
        });

    }


    public void verryGoodRating(View view){
        scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("rating").setValue("1");
        userRef.child(userP.getId()).child("score").setValue(userP.getScore()+20);

    }

    public void goodRating(View view){
        scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("rating").setValue("1");
        userRef.child(userP.getId()).child("score").setValue(userP.getScore()+10);
    }

    public void medioRating( View view){
        scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("rating").setValue("1");
        userRef.child(userP.getId()).child("score").setValue(userP.getScore()+1);
    }

    public void badRating(View view){
        scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("rating").setValue("1");
        userRef.child(userP.getId()).child("score").setValue(userP.getScore()-2);
    }

}
