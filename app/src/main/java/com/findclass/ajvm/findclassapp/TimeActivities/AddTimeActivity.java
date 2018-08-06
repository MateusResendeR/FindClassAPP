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
import com.findclass.ajvm.findclassapp.Exception.InvalidTimeException;
import com.findclass.ajvm.findclassapp.Exception.TimeFurtherThanOtherException;
import com.findclass.ajvm.findclassapp.Exception.TimeLenghtException;
import com.findclass.ajvm.findclassapp.Exception.WeekDayException;
import com.findclass.ajvm.findclassapp.Model.Date_time;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTimeActivity extends AppCompatActivity {
    private DatabaseReference db;
    private DatabaseReference professor;
    private FirebaseAuth auth;
    private String lastTimeId = new String();



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

            String[] startTimeWithoutColon = startTime.getText().toString().split(":");
            String[] endTimeWithoutColon = endTime.getText().toString().split(":");

            if (thereAreEmptyFields(startTime, endTime, day)) {
                throw new EmptyFieldException();
            } else if (startTime.getText().toString().length() < 5 || endTime.getText().toString().length() < 5){
                throw new TimeLenghtException();
            } else if (dateEndTime.before(dateStartTime)){
                throw new TimeFurtherThanOtherException();
            } else if(Integer.valueOf(startTimeWithoutColon[0]) > 23 || Integer.valueOf(endTimeWithoutColon[0]) > 23) {
                throw new InvalidTimeException();
            } else if(Integer.valueOf(startTimeWithoutColon[1]) > 59 || Integer.valueOf(endTimeWithoutColon[1]) > 59) {
                throw new InvalidTimeException();
            } else if (isDayOfWeek(day)) {
                throw new WeekDayException();
            } else {

                final Time time = new Time(startTime.getText().toString(), endTime.getText().toString(),day.getText().toString());
                professor
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("times").hasChildren()) {
                                            for (DataSnapshot d : dataSnapshot.child("times").getChildren()) {
                                                lastTimeId = d.getKey();
                                            }
                                            Integer id = Integer.valueOf(lastTimeId) + 1;
                                            professor.child("times").child(id.toString()).setValue(time);
                                        } else {
                                            professor.child("times").child("0").setValue(time);
                                        }
                                        if(dataSnapshot.child("dates").hasChildren()){
                                            for (DataSnapshot d: dataSnapshot.child("dates").getChildren()){
                                                String[] weekDay = d.getValue().toString().split(" ");
                                                if(weekDay[0].equals("Sun")
                                                        && time.getDay().equals("dom")){
                                                    Date_time dt = new Date_time(time, d.getValue().toString(),time.getDay(), "não");
                                                    DatabaseReference pushDateTime = professor.push();
                                                    pushDateTime.setValue(dt);
                                                }
                                                else if(weekDay[0].equals("Mon")
                                                        && time.getDay().equals("seg")){
                                                    Date_time dt = new Date_time(time, d.getValue().toString(),time.getDay(), "não");
                                                    DatabaseReference pushDateTime = professor.push();
                                                    pushDateTime.setValue(dt);
                                                }
                                                else if(weekDay[0].equals("Tue")
                                                        && time.getDay().equals("ter")){
                                                    Date_time dt = new Date_time(time, d.getValue().toString(),time.getDay(), "não");
                                                    DatabaseReference pushDateTime = professor.push();
                                                    pushDateTime.setValue(dt);
                                                }
                                                else if(weekDay[0].equals("Wen")
                                                        && time.getDay().equals("qua")){
                                                    Date_time dt = new Date_time(time, d.getValue().toString(),time.getDay(), "não");
                                                    DatabaseReference pushDateTime = professor.push();
                                                    pushDateTime.setValue(dt);
                                                }
                                                else if(weekDay[0].equals("Thu")
                                                        && time.getDay().equals("qui")){
                                                    Date_time dt = new Date_time(time, d.getValue().toString(),time.getDay(), "não");
                                                    DatabaseReference pushDateTime = professor.push();
                                                    pushDateTime.setValue(dt);
                                                }
                                                else if(weekDay[0].equals("Fri")
                                                        && time.getDay().equals("sex")){
                                                    Date_time dt = new Date_time(time, d.getValue().toString(),time.getDay(), "não");
                                                    DatabaseReference pushDateTime = professor.push();
                                                    pushDateTime.setValue(dt);
                                                }
                                                else if(weekDay[0].equals("Sat")
                                                        && time.getDay().equals("sab")){
                                                    Date_time dt = new Date_time(time, d.getValue().toString(),time.getDay(), "não");
                                                    DatabaseReference pushDateTime = professor.push();
                                                    pushDateTime.setValue(dt);
                                                }
                                            }
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
            } catch(TimeLenghtException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch(TimeFurtherThanOtherException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }catch(InvalidTimeException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }catch (ParseException e) {
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

    private boolean isDayOfWeek (EditText day){
        if (!day.getText().toString().equals("seg") && !day.getText().toString().equals("ter")
                && !day.getText().toString().equals("qua") && !day.getText().toString().equals("qui")
                && !day.getText().toString().equals("sex") && !day.getText().toString().equals("sab")
                && !day.getText().toString().equals("dom")) {
            return true;
        } else {
            return false;
        }
    }
    }