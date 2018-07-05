package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Exception.EmptyFieldException;
import com.findclass.ajvm.findclassapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signUp(View view){
        EditText email = (EditText) findViewById(R.id.emailEditText);
        EditText password = (EditText) findViewById(R.id.passwordEditText);

        try{
            if(!(TextUtils.isEmpty(email.getText())) && !(TextUtils.isEmpty(password.getText()))){
                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mAuth.getCurrentUser().sendEmailVerification();
                                    Toast.makeText(SignUpActivity.this, "E-mail de confirmação enviado.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getBaseContext(), SignUpStep2Activity.class);
                                    startActivity(intent);
                                }else{
                                    String message = "";
                                    try {
                                        throw task.getException();
                                    }catch (FirebaseAuthWeakPasswordException e){
                                        message = "Senha fraca, senhas precisam de no mínimo 6 caracteres.";
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        message = "E-mail inválido.";
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        message = "Esse e-mail já está sendo utilizado, tente fazer login.";
                                    } catch (Exception e) {
                                        message = "Erro ao efetuar o cadastro!";
                                    }
                                    Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }else{
                throw new EmptyFieldException();
            }
        }catch (EmptyFieldException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
