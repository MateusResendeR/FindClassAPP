package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Model.Date_Status;
import com.findclass.ajvm.findclassapp.Model.Date_Time;
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
    private final ArrayList<String> mySavedDates = new ArrayList<>();
    private final ArrayList<Time> mySavedTimes = new ArrayList<>();
    private final ArrayList<String> myTimeKeys = new ArrayList<>();

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
                try {
                    for (DataSnapshot d : dataSnapshot.child("dates").getChildren()) {
                        String dateString = d.child("date").getValue(String.class);
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                        Date today = sdf.parse(CalendarDay.today().getDate().toString());
                        Date date = sdf.parse(dateString);
                        if(date.before(today)){
                            professor.child("dates").child(d.getKey()).removeValue();
                        }
                        else{
                            mySavedDates.add(dateString);
                            materialCalendarView.setDateSelected(date, true);
                        }
                    }
                    for (DataSnapshot d : dataSnapshot.child("times").getChildren()) {
                        mySavedTimes.add(new Time(
                                d.child("startTime").getValue().toString(),
                                d.child("endTime").getValue().toString(),
                                d.child("day").getValue().toString(),
                                d.child("price").getValue().toString()
                        ));
                        myTimeKeys.add(d.getKey());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }

    public void updateSubjectDate(View view){
        try {
            auth = FirebaseAuth.getInstance();
            db = FirebaseDatabase.getInstance().getReference();
            professor = db.child("availability").child(auth.getCurrentUser().getUid());
            List<CalendarDay> calendarDays = materialCalendarView.getSelectedDates();
            for (int i = 0;i < calendarDays.size();i++) {
                boolean aux = false;
                String calendarDay = calendarDays.get(i).getDate().toString();
                for (int j = 0; j < mySavedDates.size(); j++) {
                    if (calendarDay.equals(mySavedDates.get(j))) {
                        aux = true;
                        mySavedDates.remove(j);
                        break;
                    }
                }
                if(!aux){
                    Date_Status dateStatus = new Date_Status(calendarDay, "não");
                    DatabaseReference pushDate = professor.child("dates").push();
                    pushDate.setValue(dateStatus);
                    if (mySavedTimes.size() > 0) {
                        for (int j = 0; j < mySavedTimes.size();j++) {
                            String[] weekDay = dateStatus.getDate().split(" ");
                            if (weekDay[0].equals("Sun")
                                    && mySavedTimes.get(j).getDay().equals("dom")) {
                                pushDate.child("status").setValue("sim");
                                Date_Time dt = new Date_Time(myTimeKeys.get(j), pushDate.getKey(), mySavedTimes.get(j).getDay(), "não");
                                DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                pushDateTime.setValue(dt);
                            } else if (weekDay[0].equals("Mon")
                                    && mySavedTimes.get(j).getDay().equals("seg")) {
                                pushDate.child("status").setValue("sim");
                                Date_Time dt = new Date_Time(myTimeKeys.get(j), pushDate.getKey(), mySavedTimes.get(j).getDay(), "não");
                                DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                pushDateTime.setValue(dt);
                            } else if (weekDay[0].equals("Tue")
                                    && mySavedTimes.get(j).getDay().equals("ter")) {
                                pushDate.child("status").setValue("sim");
                                Date_Time dt = new Date_Time(myTimeKeys.get(j), pushDate.getKey(), mySavedTimes.get(j).getDay(), "não");
                                DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                pushDateTime.setValue(dt);
                            } else if (weekDay[0].equals("Wed")
                                    && mySavedTimes.get(j).getDay().equals("qua")) {
                                pushDate.child("status").setValue("sim");
                                Date_Time dt = new Date_Time(myTimeKeys.get(j), pushDate.getKey(), mySavedTimes.get(j).getDay(), "não");
                                DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                pushDateTime.setValue(dt);
                            } else if (weekDay[0].equals("Thu")
                                    && mySavedTimes.get(j).getDay().equals("qui")) {
                                pushDate.child("status").setValue("sim");
                                Date_Time dt = new Date_Time(myTimeKeys.get(j), pushDate.getKey(), mySavedTimes.get(j).getDay(), "não");
                                DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                pushDateTime.setValue(dt);
                            } else if (weekDay[0].equals("Fri")
                                    && mySavedTimes.get(j).getDay().equals("sex")) {
                                pushDate.child("status").setValue("sim");
                                Date_Time dt = new Date_Time(myTimeKeys.get(j), pushDate.getKey(), mySavedTimes.get(j).getDay(), "não");
                                DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                pushDateTime.setValue(dt);
                            } else if (weekDay[0].equals("Sat")
                                    && mySavedTimes.get(j).getDay().equals("sab")) {
                                pushDate.child("status").setValue("sim");
                                Date_Time dt = new Date_Time(myTimeKeys.get(j), pushDate.getKey(), mySavedTimes.get(j).getDay(), "não");
                                DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                pushDateTime.setValue(dt);
                            }
                        }
                    }

                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addSubjectTime(View view){
        Intent intent = new Intent(this, AddTimeActivity.class);
        startActivity(intent);
    }
}