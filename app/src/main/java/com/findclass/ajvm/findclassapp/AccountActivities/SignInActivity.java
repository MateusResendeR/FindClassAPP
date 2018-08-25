package com.findclass.ajvm.findclassapp.AccountActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    //Configurações do Firebase;
    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    //Configurações para o login com o Google
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton googleSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Configurações do Firebase
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Verifica se o usuário já está logado;
        if(auth.getCurrentUser()!=null){
            isLogged();
        }

        //Configuramos o link de Esqueci minha senha;
        TextView forgotPassword = findViewById(R.id.forgotPasswordTextView);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });

        //Configuração do botão do Google para Login;
        googleSignInButton = findViewById(R.id.googleButton);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        //Configuração do Login do Google em si;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void isLogged() {
        //Cria o ProgressDialog para mostrar o processo de login;
        final ProgressDialog progressDialog = new ProgressDialog(getBaseContext());
        progressDialog.setMessage("Carregando...");
        try {
            progressDialog.show();
        }catch (Exception e){
            //pass
        }

        //Pego o usuário atual;
        final FirebaseUser currentUser = auth.getCurrentUser();

        //Verifica se o usuário está logado;
        if(auth.getCurrentUser() != null){

            //Verifica se o e-mail já foi confirmado.
            if(auth.getCurrentUser().isEmailVerified()){

                //Verifica se o usuário já tem os dados salvos no banco de dados;
                usersRef
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        //Verifica se o usuário já tem os dados salvos no banco de dados;
                                        if(dataSnapshot.hasChild(currentUser.getUid())){

                                            //Verifica se é professor ou aluno;
                                            isProfessorOrStudent(dataSnapshot.child(currentUser.getUid()),progressDialog);
                                        }else {
                                            //Caso não tenha, o usuário é deslogado do Firebase para que possa concluir seu cadastro;
                                            progressDialog.dismiss();
                                            auth.signOut();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        //Erro;
                                    }
                                }
                        );
            }else{
                //Caso a conta não esteja verificada, reenviamos o e-mail de confirmação e deslogamos o usuário do Firebase.
                auth.getCurrentUser().sendEmailVerification();
                Toast.makeText(this,"Sua conta ainda não foi verificada, enviamos um novo e-mail de confirmação",Toast.LENGTH_LONG).show();
                auth.signOut();
            }
        }
        progressDialog.dismiss();
    }

    public void signIn(View v){
        //Criação do ProgressDialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logando...");
        progressDialog.show();

        //Pego os campos que possuem os dados de login;
        EditText email = findViewById(R.id.emailEditText);
        EditText password = findViewById(R.id.passwordEditText);

        try {
            //Verifica se os campos foram preenchidos;
            if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
                throw new EmptyFieldException();
            }else{
                
                //Chama o firebase para executar o login;
                auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).
                        addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    
                                    //Verifica se o e-mail já está verificado;
                                    if (auth.getCurrentUser().isEmailVerified()){
                                        
                                        //Verifica se o usuário já tem seus dados salvos no banco de dados;
                                        isUserInDatabase(progressDialog);
                                    }else{

                                        //Caso não seja verificado, o deslogamos e enviamos um novo e-mail para confirmação;
                                        auth.getCurrentUser().sendEmailVerification();
                                        auth.signOut();
                                        String message = "Sua conta ainda não foi verificada, enviamos um novo e-mail de confirmação";
                                        Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }else{
                                    //Tratamento das possíveis exceções;
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
                                    progressDialog.dismiss();
                                    Toast.makeText(SignInActivity.this,message,Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        } catch (EmptyFieldException e){
            //Em caso de haver campos vazios;
            progressDialog.dismiss();
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void signInWithGoogle() {
        //Chama a Intent do Google;
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void forgotPassword(){
        EditText email = findViewById(R.id.emailEditText);
        try {
            //Tento fazer o envio;
            auth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        //Caso de sucesso;
                                        Toast.makeText(SignInActivity.this,"E-mail de redefinição de senha enviado.",Toast.LENGTH_LONG).show();
                                    }else{
                                        //Caso de erro;
                                        Toast.makeText(SignInActivity.this,"E-mail inválido!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                    );
        } catch (Exception e){
            //A única exceção possível é caso o campo de e-mail esteja vazio;
            Toast.makeText(
                    SignInActivity.this,
                    "Campo de e-mail vazio, por favor, preencha-o.",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void isUserInDatabase(final ProgressDialog progressDialog) {
        //Recuperar o usuário atualmente logado;
        final FirebaseUser currentUser = auth.getCurrentUser();

        //Caso haja, continuar;
        if(!(currentUser == null)){
            usersRef
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //Verificar se o usuário está no banco de dados;
                                    if(dataSnapshot.hasChild(currentUser.getUid())){
                                        //Verificar se o usuário possui endereço cadastrado no banco de dados;
                                        if(dataSnapshot.child(currentUser.getUid()).hasChild("address")){
                                            //Verificar se o usuário é professor ou aluno e abrir a activity correta;
                                            isProfessorOrStudent(dataSnapshot.child(currentUser.getUid()),progressDialog);
                                        }else {
                                            //Caso não possua endereço cadastrado no banco de dados;
                                            if(progressDialog != null){
                                                progressDialog.dismiss();
                                            }
                                            startActivity(new Intent(SignInActivity.this,SignUpStep3Activity.class));
                                        }
                                    }else {
                                        //Caso não possua nenhum dado no banco de dados;
                                        if(progressDialog != null){
                                            progressDialog.dismiss();
                                        }
                                        startActivity(new Intent(SignInActivity.this,SignUpStep2Activity.class));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //Erro;
                                }
                            }
                    );
        }
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    public void isProfessorOrStudent(DataSnapshot userSnap, ProgressDialog progressDialog){
        //Verifico se o usuário atual é professor ou aluno;
        if(userSnap.child("professor").getValue(String.class).equals("true")){
            //Abre menu de professor;
            startActivity(new Intent(this,MenuProfessorActivity.class));
        }else{
            //Abre menu de aluno;
            startActivity(new Intent(this, MenuAlunoActivity.class));
        }
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        Toast.makeText(this,"Bem-vindo, "+auth.getCurrentUser().getEmail() + "!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Verifica se o Login com o Google ocorreu corretamente;

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this,"Falha ao realizar Login com o Google!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Autenticação feita pelo Google;

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateIntentForGooleSignIn();
                        } else {
                            Toast.makeText(SignInActivity.this,"Falha ao realizar Login com o Google!",Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    private void updateIntentForGooleSignIn() {
        if (auth.getCurrentUser() != null){
            isUserInDatabase(null);
        } else {
            Toast.makeText(this,"Erro.",Toast.LENGTH_LONG).show();
        }
    }

    public void signUpIntent(View view){
        //Chama a tela de cadastro;
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }
}
