package com.findclass.ajvm.findclassapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.AccountActivities.SignInActivity;
import com.findclass.ajvm.findclassapp.AccountActivities.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.loginTextView);
        FirebaseDatabase dbRef  = FirebaseDatabase.getInstance();

        try{
            dbRef.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (auth.getCurrentUser()!=null){
                        if(!dataSnapshot.hasChild(auth.getCurrentUser().getUid().toString())){
                            auth.signOut();
                        }
                    }
                }

                public void onCancelled(DatabaseError databaseError){

                }
            });
        } catch (Exception e){
            Toast.makeText(this,e.getClass().getSimpleName(),Toast.LENGTH_LONG).show();
        }


        try{
            String email = auth.getCurrentUser().getEmail().toString();
            tv.setText(email);
        }catch (Exception e){
            startActivity(new Intent(MainActivity.this,SignInActivity.class));
        }
    }

    public void signup(View view){
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void logout(View view){
        try{
            FirebaseAuth.getInstance().signOut();
        }catch (Exception e){
            String message = "Erro, você já está delogado";
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        }
        TextView tv = (TextView) findViewById(R.id.loginTextView);
        tv.setText("Deslogado");
    }

    public void signin(View view){
        if(auth.getCurrentUser() != null){
            Toast.makeText(this,"Você já está logado!",Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(this,SignInActivity.class));
        }
    }
}
