package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.findclass.ajvm.findclassapp.R;
import com.findclass.ajvm.findclassapp.menuActivities.MenuProfessorActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyCalendarProfessorActivity extends AppCompatActivity{
    private FirebaseAuth auth;
    private DatabaseReference db;
    private DatabaseReference professor;
    private MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        professor = db.child("users").child(auth.getCurrentUser().getUid());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar_professor);

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        materialCalendarView.setSaveFromParentEnabled(true);
        materialCalendarView.setSaveEnabled(true);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();

        professor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("dates") != null) {
                        try {
                            for (int i = 0; dataSnapshot.child("dates").child(Integer.toString(i)) != null; i++) {
                                String dateString = dataSnapshot.child("dates").child(Integer.toString(i)).getValue(String.class);
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                                Date date = sdf.parse(dateString);
                                materialCalendarView.setDateSelected(date, true);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void updateSubjectDate(View view){
        try {
            List<CalendarDay> calendarDays = materialCalendarView.getSelectedDates();
            ArrayList<String> calendarDaysString = new ArrayList<String>();
            for (int i = 0;i < calendarDays.size();i++) {
                calendarDaysString.add(calendarDays.get(i).getDate().toString());
            }
            professor.child("dates").setValue(calendarDaysString);
        }catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MenuProfessorActivity.class);
        startActivity(intent);
    }
}