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

import com.findclass.ajvm.findclassapp.Exception.AlreadyRegisteredTimeException;
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

import java.util.ArrayList;


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

    //Método que finaliza a adição de um horário à disponibilidade do professor
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
            } else if(Integer.parseInt(startTimeWithoutColon[1]) > 59 || Integer.parseInt(endTimeWithoutColon[1]) > 59) {
                throw new InvalidTimeException();
            } else {
                Time time = new Time(startTime.getText().toString(), endTime.getText().toString(),
                        day.getSelectedItem().toString(),price.getText().toString());
                addTime(time);
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

    //Método que adiciona a disponibilidade de acordo com o dia da semana
    public void pushDateTimes(String[] weekDay,String key,Time time, DatabaseReference timePush){
        if (weekDay[0].equals("Sun")
                && time.getDay().equals("dom")) {
            pushDateTime(key,time,timePush);
        } else if (weekDay[0].equals("Mon")
                && time.getDay().equals("seg")) {
            pushDateTime(key,time,timePush);
        } else if (weekDay[0].equals("Tue")
                && time.getDay().equals("ter")) {
            pushDateTime(key,time,timePush);
        } else if (weekDay[0].equals("Wed")
                && time.getDay().equals("qua")) {
            pushDateTime(key,time,timePush);
        } else if (weekDay[0].equals("Thu")
                && time.getDay().equals("qui")) {
            pushDateTime(key,time,timePush);
        } else if (weekDay[0].equals("Fri")
                && time.getDay().equals("sex")) {
            pushDateTime(key,time,timePush);
        } else if (weekDay[0].equals("Sat")
                && time.getDay().equals("sab")) {
            pushDateTime(key,time,timePush);
        }
    }

    //Método que adiciona uma disponibilidade ao banco
    public void pushDateTime(String key, Time time, DatabaseReference timePush){
        professor.child("dates").child(key).child("status").setValue("sim");
        Date_Time dt = new Date_Time(timePush.getKey(), key, time.getDay(), "não");
        DatabaseReference pushDateTime = professor.child("dateTimes").push();
        pushDateTime.setValue(dt);
    }

    //Método que testa se o horário é inválido em relação à sobreposição
    public boolean invalidTimeTest(Time time, String startTime, String endTime){
        int myStartTime =
                Integer.valueOf(time.getStartTime().split(":")[0] +
                        time.getStartTime().split(":")[1]);
        int myEndTime =
                Integer.valueOf(time.getEndTime().split(":")[0] +
                        time.getEndTime().split(":")[1]);
        int intStartTime =
                Integer.valueOf(startTime.split(":")[0] +
                        startTime.split(":")[1]);
        int intEndTime =
                Integer.valueOf(endTime.split(":")[0] +
                        endTime.split(":")[1]);
        if (intStartTime >= myStartTime && intStartTime < myEndTime) {
            return true;
        } else if (myStartTime >= intStartTime && myStartTime < intEndTime) {
            return true;
        } else if (myEndTime > intStartTime && myEndTime <= intEndTime) {
            return true;
        } else if (intEndTime > myStartTime && intEndTime <= myEndTime) {
            return true;
        }
        return false;
    }

    //Método que busca horários no banco para decidir se o horário adicionado é válido
    public void addTime(Time time2) {
        final Time time = time2;
        professor.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            final ArrayList<Boolean> invalidTimeTest = new ArrayList<>();
                            for (DataSnapshot d : dataSnapshot.child("times").getChildren()) {
                                String startTime = d.child("startTime").getValue(String.class);
                                String endTime = d.child("endTime").getValue(String.class);
                                if (time.getDay().equals(d.child("day").getValue(String.class))) {
                                    if (invalidTimeTest(time, startTime, endTime)) {
                                        invalidTimeTest.add(invalidTimeTest(time, startTime, endTime));
                                        throw new AlreadyRegisteredTimeException();
                                    }
                                }
                            }
                            if (!invalidTimeTest.contains(false)) {
                                DatabaseReference timePush = professor.child("times").push();
                                timePush.setValue(time);
                                for (DataSnapshot d : dataSnapshot.child("dates").getChildren()) {
                                    String[] weekDay = d.child("date").getValue().toString().split(" ");
                                    String key = d.getKey();
                                    pushDateTimes(weekDay, key, time, timePush);
                                }
                                Intent intent = new Intent(AddTimeActivity.this, MyTimesActivity.class);
                                startActivity(intent);
                                Toast.makeText(AddTimeActivity.this, "Horário adicionado com sucesso.", Toast.LENGTH_LONG).show();
                            }
                        } catch (AlreadyRegisteredTimeException e) {
                            Toast.makeText(AddTimeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    //Método que identifica se há campos vazios na adição do horário
    private boolean thereAreEmptyFields (EditText startTime, EditText endTime){
        if (TextUtils.isEmpty(startTime.getText()) || TextUtils.isEmpty(endTime.getText())) {
            return true;
        } else {
            return false;
        }
    }

}