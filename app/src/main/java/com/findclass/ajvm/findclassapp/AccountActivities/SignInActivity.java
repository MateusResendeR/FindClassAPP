package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Exception.EmptyFieldException;
import com.findclass.ajvm.findclassapp.MainActivity;
import com.findclass.ajvm.findclassapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void signIn(View v){
        EditText email = findViewById(R.id.emailEditText);
        EditText password = findViewById(R.id.passwordEditText);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseDatabase dbRef = FirebaseDatabase.getInstance();

        try {
            if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
                throw new EmptyFieldException();
            }else{
                auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).
                        addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    if (auth.getCurrentUser().isEmailVerified()){
                                        dbRef.getReference().child("users").
                                                addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if(auth.getCurrentUser() != null) {
                                                            if(dataSnapshot.hasChild(auth.getCurrentUser().getUid().toString())){
                                                                startActivity(new Intent(SignInActivity.this,MainActivity.class));
                                                            }else {
                                                                Toast.makeText(SignInActivity.this,"Por favor, finalize seu cadastro " +auth.getCurrentUser().getEmail().toString(),
                                                                        Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(SignInActivity.this,SignUpStep2Activity.class));
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                    }else{
                                        auth.signOut();
                                        String message = "Verifique o e-mail, por favor";
                                        Toast.makeText(SignInActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    String message;
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidCredentialsException e){
                                        message = "E-mail ou senha incorretos";
                                    } catch (FirebaseAuthInvalidUserException e){
                                        message = "E-mail não cadastrado, realize cadastro";
                                    } catch (FirebaseNetworkException e){
                                        message = "Problemas de conexão";
                                    } catch (Exception e){
                                        message = "Erro";
                                    }
                                    Toast.makeText(SignInActivity.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        } catch (EmptyFieldException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void signUpIntent(View view){
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }
}
