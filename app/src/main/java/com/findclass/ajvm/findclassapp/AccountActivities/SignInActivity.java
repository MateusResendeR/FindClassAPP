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
import com.findclass.ajvm.findclassapp.menuActivities.MenuProfessorActivity;
import com.findclass.ajvm.findclassapp.menuActivities.MenuAlunoActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;

    final static FirebaseAuth auth = FirebaseAuth.getInstance();
    final static FirebaseDatabase dbRef = FirebaseDatabase.getInstance();

    SignInButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (auth.getCurrentUser() != null){
            verifyLoggedUser();
        }

        button = findViewById(R.id.googleButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void verifyLoggedUser() {
        dbRef.getReference().child("users").
                addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(auth.getCurrentUser().getUid().toString())){
                            if ((dataSnapshot.child(auth.getCurrentUser().getUid()).child("professor").getValue(String.class)).equals("true")){
                                startActivity(new Intent(SignInActivity.this,MenuProfessorActivity.class));
                                Toast.makeText(SignInActivity.this,"Bem-vindo! "+auth.getCurrentUser().getEmail(),
                                        Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                startActivity(new Intent(SignInActivity.this,MenuAlunoActivity.class));
                                Toast.makeText(SignInActivity.this,"Bem-vindo! "+auth.getCurrentUser().getEmail(),
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } else {
                            auth.signOut();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Code
                    }
                });
    }

    public void signIn(View v){
        EditText email = findViewById(R.id.emailEditText);
        EditText password = findViewById(R.id.passwordEditText);

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
                                        checkUserInDatabase();
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

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void checkUserInDatabase() {
        dbRef.getReference().child("users").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(auth.getCurrentUser() != null) {
                            if(dataSnapshot.hasChild(auth.getCurrentUser().getUid().toString())){
                                if((dataSnapshot.child(auth.getCurrentUser().getUid()).child("professor").getValue(String.class).toString()).equals("true")){
                                    startActivity(new Intent(SignInActivity.this,MenuProfessorActivity.class));
                                    Toast.makeText(SignInActivity.this,"Bem-vindo! "+auth.getCurrentUser().getEmail(),
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                }else {
                                    startActivity(new Intent(SignInActivity.this,MenuAlunoActivity.class));
                                    Toast.makeText(SignInActivity.this,"Bem-vindo! "+auth.getCurrentUser().getEmail(),
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                }

                            }else {
                                startActivity(new Intent(SignInActivity.this,SignUpStep2Activity.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Code
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this,"Falha no Login!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateIntent();
                        } else {
                            Toast.makeText(SignInActivity.this,"Erro durante Login",Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    private void updateIntent() {
        if (auth.getCurrentUser() != null){
            checkUserInDatabase();
        } else {
            Toast.makeText(this,"Erro.",Toast.LENGTH_SHORT).show();
        }
    }

    public void signUpIntent(View view){
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }
}
