package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Context;
import android.content.Intent;
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

import com.findclass.ajvm.findclassapp.MainActivity;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SignUpStep2Activity extends AppCompatActivity {
    private String email;
    private EditText password;
    private EditText name;
    private EditText surname;
    private EditText cpf;
    private EditText birthdate;
    private EditText telephony;
    private CheckBox professor;
    private DatabaseReference db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step2);
        email = getIntent().getStringExtra("EMAIL");
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        Button cadastrar = (Button) findViewById(R.id.signupButton);
        cadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signUpStep2(view);
            }
        });
    }

    public static void displayExceptionMessage(Context context, String msg) {
        Toast.makeText(context, msg , Toast.LENGTH_LONG).show();
    }

    public void signUpStep2(View view){
        password = (EditText) findViewById(R.id.passwordEditText);
        name = (EditText) findViewById(R.id.nameEditText);
        surname = (EditText) findViewById(R.id.surnameEditText);
        cpf = (EditText) findViewById(R.id.cpfEditText);
        birthdate = (EditText) findViewById(R.id.birthdateEditText);
        telephony = (EditText) findViewById(R.id.TelephoneEditText);
        professor = (CheckBox) findViewById(R.id.professorCheckBox);

        try{
            auth.createUserWithEmailAndPassword(email,password.getText().toString());

            User user = new User(email, name.getText().toString(), surname.getText().toString(), cpf.getText().toString(),
                    birthdate.getText().toString(), telephony.getText().toString(), professor.isChecked());

            String encodeValue = Base64.encodeToString((user.getEmail()).getBytes(), Base64.NO_WRAP);

            if(db != null){
                db.child("users").child(encodeValue).setValue(user);
            }

            Toast.makeText(SignUpStep2Activity.this,"Cadastro realizado com sucesso!",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SignUpStep2Activity.this, MainActivity.class);
            startActivity(intent);
        }
        catch (Exception e){
            displayExceptionMessage(this, e.getMessage());
        }


    }
}
