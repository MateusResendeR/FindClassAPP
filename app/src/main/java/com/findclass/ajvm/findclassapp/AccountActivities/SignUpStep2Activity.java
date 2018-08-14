package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Exception.CPFLenghtException;
import com.findclass.ajvm.findclassapp.Exception.DateLenghtException;
import com.findclass.ajvm.findclassapp.Exception.EmptyFieldException;
import com.findclass.ajvm.findclassapp.Exception.PhoneLenghtException;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpStep2Activity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference db;
    private String uid;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,"Conclua seu cadastro, por favor.",Toast.LENGTH_SHORT).show();

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        uid = auth.getCurrentUser().getUid().toString();
        email = auth.getCurrentUser().getEmail().toString();
        String completeName = auth.getCurrentUser().getDisplayName();
        if(completeName != null){
            String[] splitedName = completeName.split(" ");
            EditText name = findViewById(R.id.nameEditText);
            name.setText(splitedName[0]);
            EditText surname = findViewById(R.id.surnameEditText);
            surname.setText(splitedName[1]);
        }
    }

    public void finishSignUp(View view) {
        EditText name = findViewById(R.id.nameEditText);
        EditText surname = findViewById(R.id.surnameEditText);
        EditText cpf = findViewById(R.id.cpfEditText);
        EditText birthdate = findViewById(R.id.birthdateEditText);
        EditText phone = findViewById(R.id.phoneEditText);
        CheckBox professor = findViewById(R.id.professorCheckBox);

        try {
            if (thereAreEmptyFields(name, surname, cpf, birthdate, phone)) {
                throw new EmptyFieldException();
            } else if (cpf.getText().length() < 14) {
                throw new CPFLenghtException();
            } else if (birthdate.getText().length() < 10) {
                throw new DateLenghtException();
            } else if (phone.getText().length() < 14) {
                throw new PhoneLenghtException();
            } else {
                User user = new User(uid, email, name.getText().toString(),
                        surname.getText().toString(), cpf.getText().toString(), birthdate.getText().toString(),
                        phone.getText().toString(), professor.isChecked());

                if (db != null) {
                    db.child("users").child(uid).setValue(user);
                }

                if (!auth.getCurrentUser().isEmailVerified()){
                    auth.signOut();
                }

                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Cadastro realizado com sucesso.", Toast.LENGTH_SHORT).show();
            }
        } catch (EmptyFieldException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (CPFLenghtException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (DateLenghtException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (PhoneLenghtException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean thereAreEmptyFields(EditText name, EditText surname, EditText cpf, EditText birthdate, EditText phone) {
        if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(surname.getText()) ||
                TextUtils.isEmpty(cpf.getText()) || TextUtils.isEmpty(birthdate.getText()) ||
                TextUtils.isEmpty(phone.getText())) {
            return true;
        } else {
            return false;
        }
    }

}
