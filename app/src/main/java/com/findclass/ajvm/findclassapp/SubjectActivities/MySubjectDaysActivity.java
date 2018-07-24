package com.findclass.ajvm.findclassapp.SubjectActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.R;
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

public class MySubjectDaysActivity extends AppCompatActivity{
    private FirebaseAuth auth;
    private DatabaseReference db;
    private DatabaseReference professorSubject;
    private MaterialCalendarView materialCalendarView;
    private Subject subject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        professorSubject = db.child("professorSubjects").child(auth.getCurrentUser().getUid());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subject_days);

        Bundle data = getIntent().getExtras();
        subject = (Subject) data.getSerializable("subject");

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        materialCalendarView.setSaveFromParentEnabled(true);
        materialCalendarView.setSaveEnabled(true);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();

        professorSubject.child(subject.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.child("dates") != null) {
                        ArrayList<Date> dates = new ArrayList<Date>();
                        for (int i = 0; dataSnapshot.child("dates").child(Integer.toString(i)) != null; i++) {
                            String dateString = dataSnapshot.child("dates").child(Integer.toString(i)).getValue(String.class);
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                            Date date = sdf.parse(dateString);
                            dates.add(date);
                            materialCalendarView.setDateSelected(date,true);
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(MySubjectDaysActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            professorSubject.child(subject.getId()).child("dates").setValue(calendarDaysString);
        }catch (Exception e){
            Toast.makeText(MySubjectDaysActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}