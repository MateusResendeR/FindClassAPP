package com.findclass.ajvm.findclassapp.menuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Exception.CanNotCancelException;
import com.findclass.ajvm.findclassapp.Model.Address;
import com.findclass.ajvm.findclassapp.Model.Date_Status;
import com.findclass.ajvm.findclassapp.Model.Schedule;
import com.findclass.ajvm.findclassapp.Model.ScheduleObject;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class InfoScheduleTeacherActivity extends AppCompatActivity {
    private User student;
    private User professor;
    private Subject subject;
    private ScheduleObject schedule;
    private DatabaseReference userRef;
    private DatabaseReference availabilityRef;
    private DatabaseReference scheduleRef;
    private User userP;
    private User userS;
    private Date_Status date;
    private String dateTime;
    private int cancel = 0;
    private Date realDate;
    private TextView textViewSubject;
    private TextView textViewLevel;
    private TextView textViewAluno;
    private TextView textViewDate;
    private TextView textViewTime;

    private TextView cityUfTextView;
    private TextView districtTextView;
    private TextView addressTextView;
    private TextView numberComplementTextView;

    private ValueEventListener valueEventListenerP;
    private ValueEventListener valueEventListenerS;
    private ValueEventListener valueEventListenerD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_scedule_teacher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        availabilityRef = FirebaseDatabase.getInstance().getReference().child("availability");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        scheduleRef = FirebaseDatabase.getInstance().getReference().child("schedule");

        textViewSubject = findViewById(R.id.infoSubjectNameTextView);
        textViewLevel = findViewById(R.id.infoSubjectLevelTextView);
        textViewAluno = findViewById(R.id.infoAlunoNameTextView);
        textViewDate = findViewById(R.id.infoDateTextView);
        textViewTime = findViewById(R.id.infoTimeTextView);

        cityUfTextView = findViewById(R.id.cityUfTextView);
        districtTextView = findViewById(R.id.districtTextView);
        addressTextView = findViewById(R.id.addressTextView);
        numberComplementTextView = findViewById(R.id.numberComplementTextView);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            schedule = (ScheduleObject)bundle.getSerializable("schedule");
            String dateString = schedule.getDate().getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            realDate = new Date();
            try {
                realDate = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            student = schedule.getStudent();
            professor = schedule.getProfessor();
            subject = schedule.getSubject();

            textViewDate.setText(dateFormat.format(realDate)+" ("+schedule.getTime().getDay()+")");
            textViewAluno.setText(student.getName());
            textViewSubject.setText(subject.getName());
            textViewLevel.setText(subject.getLevel());
            textViewTime.setText(schedule.getTime().getStartTime()+" - "+schedule.getTime().getEndTime());

            Address scheduleAddress = professor.getAddress();
            String cityUf = (scheduleAddress.getCity()+"/"+scheduleAddress.getState());
            String district = (scheduleAddress.getDistrict());
            String address = (scheduleAddress.getAddress());
            String numberComplement = (String.valueOf(scheduleAddress.getNumber()));
            if(!TextUtils.isEmpty(scheduleAddress.getComplement())){
                numberComplement+=(", "+scheduleAddress.getComplement());
            }

            Log.e("FON",cityUf);

            cityUfTextView.setText(cityUf);
            districtTextView.setText(district);
            addressTextView.setText(address);
            numberComplementTextView.setText(numberComplement);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    protected void onStart() {
        super.onStart();
        getProfessor();
        getStudent();
        getDate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerP);
        userRef.removeEventListener(valueEventListenerS);
        availabilityRef.removeEventListener(valueEventListenerD);
    }

    public void retrieveCancel(final String schedule_id){

        scheduleRef.child(userP.getId())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    for (DataSnapshot scheduleSnap: dataSnapshot1.getChildren()){
                                        if(scheduleSnap.getKey().equals(schedule_id)){
                                            cancel = scheduleSnap.getValue(Schedule.class).getCancel();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
    }

    public void getProfessor(){

        valueEventListenerP = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(User.class).getId().equals(professor.getId())){
                        userP = data.getValue(User.class);
                        retrieveCancel(schedule.getId());
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
                        getDateTimeId();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getDate(){
        valueEventListenerD = availabilityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.child(userP.getId()).child("dates").getChildren()){
                    if(data.child("date").getValue(String.class).equals(schedule.getDate().getDate())){
                        date = data.getValue(Date_Status.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getDateTimeId(){
        scheduleRef.child(userP.getId()).child(userS.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            if(data.getKey().equals(schedule.getId())){
                                dateTime = data.getValue(Schedule.class).getDatetime_id();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void cancelTeacher(View view){
        try {
            DateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
            SimpleDateFormat oldFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Date MyDate = oldFormat.parse(date.getDate());
            String MyDateString = myFormat.format(MyDate);
            String today = myFormat.format(CalendarDay.today().getDate());
            long diff = (myFormat.parse(MyDateString).getTime()) - (myFormat.parse(today).getTime());
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            if (days >= 2 && cancel == 0) {
                scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("cancel").setValue(1);
                availabilityRef.child(userP.getId()).child("dateTimes").child(dateTime).child("status").setValue("não");
                Intent intent = new Intent(getBaseContext(), MenuProfessorActivity.class);
                startActivity(intent);
            } else {
                throw new CanNotCancelException();
            }
        }
        catch (CanNotCancelException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void finish(View view){
        scheduleRef.child(userP.getId()).child(userS.getId()).child(schedule.getId()).child("finish").setValue(1);
        Intent intent = new Intent(getBaseContext(), MenuAlunoActivity.class);
        startActivity(intent);
    }


}
