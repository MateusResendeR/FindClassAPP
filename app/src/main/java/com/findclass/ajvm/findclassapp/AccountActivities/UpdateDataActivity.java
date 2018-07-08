package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Exception.EmptyFieldException;
import com.findclass.ajvm.findclassapp.Exception.PhoneLenghtException;
import com.findclass.ajvm.findclassapp.R;
import com.findclass.ajvm.findclassapp.menuActivities.MenuProfessorActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UpdateDataActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        db.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String name = dataSnapshot.child(auth.getCurrentUser().getUid()).child("name").getValue(String.class);
                    String surname = dataSnapshot.child(auth.getCurrentUser().getUid()).child("surname").getValue(String.class);
                    String phone = dataSnapshot.child(auth.getCurrentUser().getUid()).child("telephony").getValue(String.class);
                    ((TextView) findViewById(R.id.nameEditText)).setText(name);
                    ((TextView) findViewById(R.id.surnameEditText)).setText(surname);
                    ((TextView) findViewById(R.id.phoneEditText)).setText(phone);
                }catch (Exception e){
                    Toast.makeText(UpdateDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void finishUpdateData(View view) throws EmptyFieldException, PhoneLenghtException {
        EditText name = findViewById(R.id.nameEditText);
        EditText surname = findViewById(R.id.surnameEditText);
        EditText phone = findViewById(R.id.phoneEditText);
        try{
            if (thereAreEmptyFields(name, surname, phone)) {
                throw new EmptyFieldException();
            } else if (phone.getText().length() < 14) {
                throw new PhoneLenghtException();
            } else {
                db.child("users").child(auth.getCurrentUser().getUid()).child("name").setValue(name.getText().toString());
                db.child("users").child(auth.getCurrentUser().getUid()).child("surname").setValue(surname.getText().toString());
                db.child("users").child(auth.getCurrentUser().getUid()).child("telephony").setValue(phone.getText().toString());

                Toast.makeText(this, "Alteração de dados realizada com sucesso.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MenuProfessorActivity.class);
                startActivity(intent);
            }
        } catch (EmptyFieldException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (PhoneLenghtException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean thereAreEmptyFields(EditText name, EditText surname, EditText phone) {
        if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(surname.getText()) ||
                TextUtils.isEmpty(phone.getText())) {
            return true;
        } else {
            return false;
        }
    }

}
