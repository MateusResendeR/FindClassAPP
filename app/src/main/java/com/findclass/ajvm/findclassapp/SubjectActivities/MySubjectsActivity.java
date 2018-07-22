package com.findclass.ajvm.findclassapp.SubjectActivities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.R;

public class MySubjectsActivity extends AppCompatActivity {
    FloatingActionButton addSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subjects);

        addSubject = findViewById(R.id.addSubjectFloatingActionButton);

        addSubject
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getBaseContext(),AddSubjectActivity.class);
                                startActivity(intent);
                            }
                        }
                );
    }
}
