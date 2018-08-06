package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Model.Date_time;
import com.findclass.ajvm.findclassapp.Model.Time;
import com.findclass.ajvm.findclassapp.R;
import com.findclass.ajvm.findclassapp.TimeActivities.AddTimeActivity;
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
    private final ArrayList<Integer> count = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        professor = db.child("availability").child(auth.getCurrentUser().getUid());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar_professor);

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        materialCalendarView.setSaveFromParentEnabled(true);
        materialCalendarView.setSaveEnabled(true);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .setMinimumDate(CalendarDay.today())
                .commit();

        professor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("dates") != null) {
                        try {
                            for (int i = 0; dataSnapshot.child("dates").child(Integer.toString(i)) != null; i++) {
                                count.add(i);
                                String dateString = dataSnapshot.child("dates").child(Integer.toString(i)).getValue(String.class);
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                                Date today = sdf.parse(CalendarDay.today().getDate().toString());
                                Date date = sdf.parse(dateString);
                                if(date.before(today)){
                                    professor.child("dates").child(Integer.toString(i)).removeValue();
                                }
                                else{
                                    materialCalendarView.setDateSelected(date, true);
                                }
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
            final List<CalendarDay> calendarDays = materialCalendarView.getSelectedDates();
            final ArrayList<String> calendarDaysString = new ArrayList<String>();
            for (int i = 0;i < calendarDays.size();i++) {
                calendarDaysString.add(calendarDays.get(i).getDate().toString());
            }

            professor.child("dates").setValue(calendarDaysString);
            professor.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (count != null) {
                        for (int i = count.size(); dataSnapshot.child("dates").child(Integer.toString(i)) != null; i++) {
                            String dateString = dataSnapshot.child("dates").child(Integer.toString(i)).getValue(String.class);
                            if (dataSnapshot.child("times").hasChildren() && dataSnapshot.child("dates").child(Integer.toString(i)) != null) {
                                for (DataSnapshot d : dataSnapshot.child("times").getChildren()) {
                                    String[] weekDay = dateString.split(" ");
                                    Time time = new Time(d.child("startTime").getValue().toString(),
                                            d.child("endTime").getValue().toString(), d.child("day").getValue().toString());
                                    Toast.makeText(MyCalendarProfessorActivity.this, time.getDay().toString() + weekDay[0], Toast.LENGTH_SHORT).show();
                                    if (time.getDay().equals("dom")
                                            && weekDay[0].equals("Sun")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("seg")
                                            && weekDay[0].equals("Mon")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("ter")
                                            && weekDay[0].equals("Tue")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("qua")
                                            && weekDay[0].equals("Wen")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("qui")
                                            && weekDay[0].equals("Thu")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("sex")
                                            && weekDay[0].equals("Fri")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("sab")
                                            && weekDay[0].equals("Sat")) {
                                        Date_time dt = new Date_time(time, d.getValue().toString(), time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MenuProfessorActivity.class);
        startActivity(intent);
    }

    public void addSubjectTime(View view){
        try {
            final List<CalendarDay> calendarDays = materialCalendarView.getSelectedDates();
            final ArrayList<String> calendarDaysString = new ArrayList<String>();
            for (int i = 0;i < calendarDays.size();i++) {
                calendarDaysString.add(calendarDays.get(i).getDate().toString());
            }
            professor.child("dates").setValue(calendarDaysString);

            professor.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (count != null && count.size() < dataSnapshot.child("dates").getChildrenCount()) {
                        for (int i = count.size(); dataSnapshot.child("dates").child(Integer.toString(i)) != null; i++) {
                            String dateString = dataSnapshot.child("dates").child(Integer.toString(i)).getValue(String.class);
                            if (dataSnapshot.child("times").hasChildren()) {
                                for (DataSnapshot d : dataSnapshot.child("times").getChildren()) {
                                    String[] weekDay = dateString.split(" ");
                                    Time time = new Time(d.child("startTime").getValue().toString(),
                                            d.child("endTime").getValue().toString(), d.child("day").getValue().toString());
                                    Toast.makeText(MyCalendarProfessorActivity.this, time.getDay().toString() + weekDay[0], Toast.LENGTH_SHORT).show();
                                    if (time.getDay().equals("dom")
                                            && weekDay[0].equals("Sun")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("seg")
                                            && weekDay[0].equals("Mon")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("ter")
                                            && weekDay[0].equals("Tue")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("qua")
                                            && weekDay[0].equals("Wen")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("qui")
                                            && weekDay[0].equals("Thu")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("sex")
                                            && weekDay[0].equals("Fri")) {
                                        Date_time dt = new Date_time(time, dateString, time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    } else if (time.getDay().equals("sab")
                                            && weekDay[0].equals("Sat")) {
                                        Date_time dt = new Date_time(time, d.getValue().toString(), time.getDay(), "não");
                                        DatabaseReference pushDateTime = professor.push();
                                        pushDateTime.setValue(dt);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = new Intent(this, AddTimeActivity.class);
        startActivity(intent);
    }
}