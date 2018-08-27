package com.findclass.ajvm.findclassapp.aboutActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.findclass.ajvm.findclassapp.R;
import com.findclass.ajvm.findclassapp.menuActivities.MenuAlunoActivity;
import com.findclass.ajvm.findclassapp.menuActivities.MenuProfessorActivity;

public class StudentAboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MenuAlunoActivity.class));
        finish();
    }
}
