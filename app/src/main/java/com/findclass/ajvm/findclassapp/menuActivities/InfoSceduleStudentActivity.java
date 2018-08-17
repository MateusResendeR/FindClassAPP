package com.findclass.ajvm.findclassapp.menuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.findclass.ajvm.findclassapp.Model.ScheduleObject;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoSceduleStudentActivity extends AppCompatActivity {
    private User professor;
    private Subject subject;
    private ScheduleObject schedule;
    private DatabaseReference userRef;
    private DatabaseReference scheduleRef;
    private User userP;
    private User userS;
    private ValueEventListener valueEventListenerP;
    private ValueEventListener valueEventListenerS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_scedule_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        scheduleRef = FirebaseDatabase.getInstance().getReference().child("schedule");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            schedule = (ScheduleObject)bundle.getSerializable("schedule");
            professor = schedule.getProfessor();
            subject = schedule.getSubject();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
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

    public void finish(View view){
        scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("finish").setValue(1);
        Intent intent = new Intent(getBaseContext(), MenuAlunoActivity.class);
        startActivity(intent);
    }



}
