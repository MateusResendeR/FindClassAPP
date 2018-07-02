package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Exception.CPFLenghtException;
import com.findclass.ajvm.findclassapp.Exception.DateLenghtException;
import com.findclass.ajvm.findclassapp.Exception.EmptyFieldException;
import com.findclass.ajvm.findclassapp.Exception.PhoneLenghtException;
import com.findclass.ajvm.findclassapp.MainActivity;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Date;

public class SignUpStep2Activity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step2);
    }

    public void finishSignUp(View view){
        EditText name = (EditText) findViewById(R.id.nameEditText);
        EditText surname = (EditText) findViewById(R.id.surnameEditText);
        EditText cpf = (EditText) findViewById(R.id.cpfEditText);
        EditText birthdate = (EditText) findViewById(R.id.birthdateEditText);
        EditText phone = (EditText) findViewById(R.id.phoneEditText);
        CheckBox professor = (CheckBox) findViewById(R.id.professorCheckBox);

        try{
            if(cpf.getText().length() < 14){
                throw new CPFLenghtException();
            }else{
                try{
                    if(birthdate.getText().length() < 10){
                        throw new DateLenghtException();
                    }else{
                        try{
                            if(phone.getText().length() < 14){
                                throw new PhoneLenghtException();
                            }else{
                                try{
                                    if(thereAreEmptyFields(name,surname,cpf,birthdate,phone)){
                                        throw new EmptyFieldException();
                                    }else{
                                        User user = new User(auth.getCurrentUser().getEmail(),name.getText().toString(),
                                                surname.getText().toString(), cpf.getText().toString(), birthdate.getText().toString(),
                                                phone.getText().toString(), professor.isChecked());
                                        if (db!=null){
                                            db.child("users").child(auth.getCurrentUser().getUid().toString()).setValue(user);
                                        }

                                        Toast.makeText(this,"Cadastro finalizado com sucesso!",
                                                Toast.LENGTH_SHORT).show();

                                        auth.getCurrentUser().sendEmailVerification().
                                                addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(SignUpStep2Activity.this,"E-mail de confirmação enviado.",Toast.LENGTH_SHORT).
                                                            show();
                                                }
                                            }
                                        });

                                        Intent intent = new Intent(this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }catch (EmptyFieldException e){
                                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (PhoneLenghtException e){
                            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (DateLenghtException e){
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }catch (CPFLenghtException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private boolean thereAreEmptyFields(EditText name, EditText surname, EditText cpf, EditText birthdate, EditText phone){
        if((TextUtils.isEmpty(name.getText())) || (TextUtils.isEmpty(surname.getText())) ||
                (TextUtils.isEmpty(cpf.getText())) || (TextUtils.isEmpty(birthdate.getText())) ||
                (TextUtils.isEmpty(phone.getText()))){
            return true;
        }else{
            return false;
        }
    }
}
