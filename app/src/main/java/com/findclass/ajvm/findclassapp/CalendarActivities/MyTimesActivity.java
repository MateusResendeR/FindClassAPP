package com.findclass.ajvm.findclassapp.CalendarActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.findclass.ajvm.findclassapp.R;

public class MyTimesActivity extends AppCompatActivity {
    FloatingActionButton addTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_times);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addTime = findViewById(R.id.addTimeFloatingActionButton);

        addTime
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getBaseContext(),AddTimeActivity.class);
                                startActivity(intent);
                            }
                        }
                );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
