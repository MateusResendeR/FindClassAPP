package com.findclass.ajvm.findclassapp.CalendarActivities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Exception.EmptyFieldException;
import com.findclass.ajvm.findclassapp.Exception.InvalidTimeException;
import com.findclass.ajvm.findclassapp.Exception.TimeFurtherThanOtherException;
import com.findclass.ajvm.findclassapp.Exception.FieldLenghtException;
import com.findclass.ajvm.findclassapp.Model.Date_Time;
import com.findclass.ajvm.findclassapp.Model.Time;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vicmikhailau.maskededittext.MaskedEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTimeActivity extends AppCompatActivity {
    private DatabaseReference db;
    private DatabaseReference professor;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        professor = db.child("availability").child(auth.getCurrentUser().getUid());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);
    }

    public void finishAddSubjectTime(View view) {
        MaskedEditText startTime = findViewById(R.id.startTimeEditText);
        MaskedEditText endTime = findViewById(R.id.endTimeEditText);
        MaskedEditText price = findViewById(R.id.priceEditText);
        Spinner day = findViewById(R.id.daySpinner);

        try {

            String[] startTimeWithoutColon = startTime.getText().toString().split(":");
            String[] endTimeWithoutColon = endTime.getText().toString().split(":");

            if (thereAreEmptyFields(startTime, endTime)) {
                throw new EmptyFieldException();
            } else if (startTime.getText().toString().length() < 5 || endTime.getText().toString().length() < 5
                    || price.getText().toString().length() < 7){
                throw new FieldLenghtException();
            } else if (Integer.parseInt(startTimeWithoutColon[0]) > Integer.parseInt(endTimeWithoutColon[0])){
                throw new TimeFurtherThanOtherException();
            } else if (Integer.parseInt(startTimeWithoutColon[0]) == Integer.parseInt(endTimeWithoutColon[0])){
                if (Integer.parseInt(startTimeWithoutColon[1]) >= Integer.parseInt(endTimeWithoutColon[1])) {
                    throw new TimeFurtherThanOtherException();
                }
            } else if(Integer.parseInt(startTimeWithoutColon[0]) > 23 || Integer.parseInt(endTimeWithoutColon[0]) > 23) {
                throw new InvalidTimeException();
            } else if(Integer.parseInt(startTimeWithoutColon[1]) == 59 || Integer.parseInt(endTimeWithoutColon[1]) > 59) {
                throw new InvalidTimeException();
            } else {

                final Time time = new Time(startTime.getText().toString(), endTime.getText().toString(),
                        day.getSelectedItem().toString(),price.getText().toString());
                professor
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            for (DataSnapshot d : dataSnapshot.child("times").getChildren()) {
                                                if (time.getDay().equals(d.child("day").getValue(String.class))) {
                                                    int myStartTime =
                                                            Integer.valueOf(time.getStartTime().split(":")[0]+
                                                                    time.getStartTime().split(":")[1]);
                                                    int myEndTime =
                                                            Integer.valueOf(time.getEndTime().split(":")[0]+
                                                                    time.getEndTime().split(":")[1]);
                                                    int intStartTime =
                                                            Integer.valueOf(d.child("startTime").getValue(String.class).split(":")[0]+
                                                                    d.child("startTime").getValue(String.class).split(":")[1]);
                                                    int intEndTime =
                                                            Integer.valueOf(d.child("endTime").getValue(String.class).split(":")[0]+
                                                                    d.child("endTime").getValue(String.class).split(":")[1]);
                                                    if (intStartTime >= myStartTime && intStartTime < myEndTime) {
                                                        throw new InvalidTimeException();
                                                    } else if (myStartTime >= intStartTime && myStartTime < intEndTime) {
                                                        throw new InvalidTimeException();
                                                    } else if (myEndTime > intStartTime && myEndTime <= intEndTime) {
                                                        throw new InvalidTimeException();
                                                    } else if (intEndTime > myStartTime && intEndTime <= myEndTime) {
                                                        throw new InvalidTimeException();
                                                    }
                                                }
                                            }
                                            DatabaseReference timePush = professor.child("times").push();
                                            timePush.setValue(time);
                                            if (dataSnapshot.child("dates").hasChildren()) {
                                                for (DataSnapshot d : dataSnapshot.child("dates").getChildren()) {
                                                    String[] weekDay = d.child("date").getValue().toString().split(" ");
                                                    if (weekDay[0].equals("Sun")
                                                            && time.getDay().equals("dom")) {
                                                        professor.child("dates").child(d.getKey()).child("status").setValue("sim");
                                                        Date_Time dt = new Date_Time(timePush.getKey(), d.getKey(), time.getDay(), "não");
                                                        DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                                        pushDateTime.setValue(dt);
                                                    } else if (weekDay[0].equals("Mon")
                                                            && time.getDay().equals("seg")) {
                                                        professor.child("dates").child(d.getKey()).child("status").setValue("sim");
                                                        Date_Time dt = new Date_Time(timePush.getKey(), d.getKey(), time.getDay(), "não");
                                                        DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                                        pushDateTime.setValue(dt);
                                                    } else if (weekDay[0].equals("Tue")
                                                            && time.getDay().equals("ter")) {
                                                        professor.child("dates").child(d.getKey()).child("status").setValue("sim");
                                                        Date_Time dt = new Date_Time(timePush.getKey(), d.getKey(), time.getDay(), "não");
                                                        DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                                        pushDateTime.setValue(dt);
                                                    } else if (weekDay[0].equals("Wed")
                                                            && time.getDay().equals("qua")) {
                                                        professor.child("dates").child(d.getKey()).child("status").setValue("sim");
                                                        Date_Time dt = new Date_Time(timePush.getKey(), d.getKey(), time.getDay(), "não");
                                                        DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                                        pushDateTime.setValue(dt);
                                                    } else if (weekDay[0].equals("Thu")
                                                            && time.getDay().equals("qui")) {
                                                        professor.child("dates").child(d.getKey()).child("status").setValue("sim");
                                                        Date_Time dt = new Date_Time(timePush.getKey(), d.getKey(), time.getDay(), "não");
                                                        DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                                        pushDateTime.setValue(dt);
                                                    } else if (weekDay[0].equals("Fri")
                                                            && time.getDay().equals("sex")) {
                                                        professor.child("dates").child(d.getKey()).child("status").setValue("sim");
                                                        Date_Time dt = new Date_Time(timePush.getKey(), d.getKey(), time.getDay(), "não");
                                                        DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                                        pushDateTime.setValue(dt);
                                                    } else if (weekDay[0].equals("Sat")
                                                            && time.getDay().equals("sab")) {
                                                        professor.child("dates").child(d.getKey()).child("status").setValue("sim");
                                                        Date_Time dt = new Date_Time(timePush.getKey(), d.getKey(), time.getDay(), "não");
                                                        DatabaseReference pushDateTime = professor.child("dateTimes").push();
                                                        pushDateTime.setValue(dt);
                                                    }
                                                }
                                            }
                                        } catch (Exception e){
                                            Toast.makeText(AddTimeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();;
                                        }

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }

                        );

                    Intent intent = new Intent(this, MyTimesActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Horário adicionado com sucesso.", Toast.LENGTH_LONG).show();
                }
            } catch(EmptyFieldException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            } catch(FieldLenghtException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            } catch(TimeFurtherThanOtherException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }catch(InvalidTimeException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e){
                e.printStackTrace();
            }
    }

        private boolean thereAreEmptyFields (EditText startTime, EditText endTime){
            if (TextUtils.isEmpty(startTime.getText()) || TextUtils.isEmpty(endTime.getText())) {
                return true;
            } else {
                return false;
            }
        }


}