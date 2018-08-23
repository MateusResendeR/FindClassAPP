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
import com.findclass.ajvm.findclassapp.Exception.MaxAgeException;
import com.findclass.ajvm.findclassapp.Exception.MinAgeException;
import com.findclass.ajvm.findclassapp.Exception.PhoneLenghtException;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        Toast.makeText(this,"Conclua seu cadastro, por favor.",Toast.LENGTH_LONG).show();

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

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);

        try {
            ArrayList<EditText> editTexts = new ArrayList<>();
            editTexts.add(name);
            editTexts.add(surname);
            editTexts.add(cpf);
            editTexts.add(birthdate);
            editTexts.add(phone);

            if (thereAreEmptyFields(editTexts)) {
                throw new EmptyFieldException();
            } else if (cpf.getText().length() < 14) {
                throw new CPFLenghtException();
            } else if (birthdate.getText().length() < 10) {
                throw new DateLenghtException();
            } else if (phone.getText().length() < 14) {
                throw new PhoneLenghtException();
            } else {
                String birthdateTest = birthdate.getText().toString();
                Date thisBirthdate = df.parse(birthdateTest);

                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.YEAR,-100);
                Date minimunDate = calendar.getTime();

                calendar.add(Calendar.YEAR,90);
                Date maximumDate = calendar.getTime();

                if(thisBirthdate.after(maximumDate)){
                    throw new MinAgeException();
                }

                if(thisBirthdate.before(minimunDate)){
                    throw new MaxAgeException();
                }

                User user = new User(uid, email, name.getText().toString(),
                        surname.getText().toString(), cpf.getText().toString(), birthdate.getText().toString(),
                        phone.getText().toString(), professor.isChecked());

                if (db != null) {
                    db.child("users").child(uid).setValue(user);
                }

                Intent intent = new Intent(this, SignUpStep3Activity.class);
                startActivity(intent);
            }
        } catch (EmptyFieldException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (CPFLenghtException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (DateLenghtException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (PhoneLenghtException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
             Toast.makeText(this, "Data de nascimento invalida!", Toast.LENGTH_LONG).show();
        } catch (MaxAgeException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (MinAgeException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            auth.signOut();
        }catch (Exception e){
            //Does not need any other action.
        }
        startActivity(new Intent(this,SignInActivity.class));
        finish();
    }

    private boolean thereAreEmptyFields(ArrayList<EditText> editTexts) {
        for (EditText field: editTexts){
            if(TextUtils.isEmpty(field.getText())){
                return true;
            }
        }
        return false;
    }

}
