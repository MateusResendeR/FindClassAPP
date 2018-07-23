package com.findclass.ajvm.findclassapp.SubjectActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.R;

public class MySubjectInfoActivity extends AppCompatActivity {
    private Subject subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subject_info);

        Bundle data = getIntent().getExtras();
        subject = (Subject) data.getSerializable("subject");

        Toast.makeText(this, subject.getId(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, subject.getName(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, subject.getLevel(), Toast.LENGTH_SHORT).show();
    }
}
