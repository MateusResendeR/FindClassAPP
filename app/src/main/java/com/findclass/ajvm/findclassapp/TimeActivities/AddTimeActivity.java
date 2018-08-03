package com.findclass.ajvm.findclassapp.TimeActivities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.AccountActivities.MyCalendarProfessorActivity;
import com.findclass.ajvm.findclassapp.Exception.EmptyFieldException;
import com.findclass.ajvm.findclassapp.Exception.WeekDayException;
import com.findclass.ajvm.findclassapp.Model.Time;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vicmikhailau.maskededittext.MaskedEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Boolean.FALSE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTimeActivity extends AppCompatActivity {
    private DatabaseReference db;
    private DatabaseReference professor;
    private FirebaseAuth auth;
    private String lastTimeId = new String();

    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        professor = db.child("availability").child(auth.getCurrentUser().getUid());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_activity);
    }

    public void finishAddSubjectTime(View view) {
        MaskedEditText startTime = findViewById(R.id.startTimeEditText);
        MaskedEditText endTime = findViewById(R.id.endTimeEditText);
        EditText day = findViewById(R.id.dayEditText);

        try {

            DateFormat sdf = new SimpleDateFormat("hh:mm");
            Date dateStartTime = sdf.parse(startTime.getText().toString());
            Date dateEndTime = sdf.parse(endTime.getText().toString());
            if (thereAreEmptyFields(startTime, endTime, day)) {
                throw new EmptyFieldException();
            } else if (!day.getText().toString().equals("seg") && !day.getText().toString().equals("ter")
                    && !day.getText().toString().equals("qua") && !day.getText().toString().equals("qui")
                    && !day.getText().toString().equals("sex") && !day.getText().toString().equals("sab")
                    && !day.getText().toString().equals("dom")) {
                throw new WeekDayException();
            } else {

                final Time time = new Time(startTime.getText().toString(), endTime.getText().toString(),
                        day.getText().toString());
                    valueEventListener = professor
                            .child("times")
                            .addValueEventListener(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                lastTimeId = d.getKey();

                                            }
                                            Integer id = Integer.valueOf(lastTimeId);
                                            id++;
                                            if(lastTimeId == null){
                                                professor.child("times").child("1").setValue(time);
                                            }
                                            else {
                                                Toast.makeText(AddTimeActivity.this, (id).toString(), Toast.LENGTH_SHORT).show();
                                                professor.child("times").child((id).toString()).setValue(time);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }

                                    }

                            );


                Intent intent = new Intent(this, MyCalendarProfessorActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Horário adicionado com sucesso.", Toast.LENGTH_SHORT).show();
            }
            } catch(EmptyFieldException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch(WeekDayException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
    }



        private boolean thereAreEmptyFields (EditText startTime, EditText endTime, EditText day){
            if (TextUtils.isEmpty(startTime.getText()) || TextUtils.isEmpty(endTime.getText()) ||
                    TextUtils.isEmpty(day.getText())) {
                return true;
            } else {
                return false;
            }
        }
    }