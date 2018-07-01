package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signUp(View view){
        email = (EditText) findViewById(R.id.emailEditText);

        Random r = new Random();
        int tempPassword = r.nextInt(89999999) + 1000000;

        mAuth = FirebaseAuth.getInstance();

        if(email.getText().length() > 0){
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),String.valueOf(tempPassword))
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete();
                                Intent intent = new Intent(getBaseContext(), SignUpStep2Activity.class);
                                intent.putExtra("EMAIL", email.getText().toString());
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUpActivity.this, "E-mail inválido ou já em uso",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(SignUpActivity.this,"E-mail inválido",Toast.LENGTH_SHORT).show();
        }
    }
}
