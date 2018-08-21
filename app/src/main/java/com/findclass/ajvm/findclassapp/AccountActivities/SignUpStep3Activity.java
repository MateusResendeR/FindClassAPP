package com.findclass.ajvm.findclassapp.AccountActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Exception.EmptyFieldException;
import com.findclass.ajvm.findclassapp.Exception.LenghtException;
import com.findclass.ajvm.findclassapp.Model.Address;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpStep3Activity extends AppCompatActivity {
    Button finishButton;
    EditText cepET;
    EditText stateET;
    EditText cityET;
    EditText districtET;
    EditText addressET;
    EditText numberET;
    EditText complementET;

    DatabaseReference usersRef;
    FirebaseAuth auth;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step3);

        finishButton = findViewById(R.id.buttonFinish);
        cepET = findViewById(R.id.editTextCep);
        stateET = findViewById(R.id.editTextState);
        cityET = findViewById(R.id.editTextCity);
        districtET = findViewById(R.id.editTextDistrict);
        addressET = findViewById(R.id.editTextAddress);
        numberET = findViewById(R.id.editTextNumber);
        complementET = findViewById(R.id.editTextComplement);

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        auth = FirebaseAuth.getInstance();

        uid = auth.getCurrentUser().getUid();

        auth.signOut();

        finishButton
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addressSingUp();
                            }
                        }
                );
    }

    public void addressSingUp(){

        try {
            ArrayList<EditText> editTexts = new ArrayList<>();
            editTexts.add(cepET);
            editTexts.add(stateET);
            editTexts.add(cityET);
            editTexts.add(districtET);
            editTexts.add(addressET);
            editTexts.add(numberET);

            if(thereAreEmptyFields(editTexts)){
                throw new EmptyFieldException();
            }

            String cep = cepET.getText().toString();

            if(cep.length() != 9){
                throw new LenghtException();
            }

            String state = stateET.getText().toString();
            String city = cityET.getText().toString();
            String district = districtET.getText().toString();
            String address = addressET.getText().toString();
            int number = Integer.valueOf(numberET.getText().toString());

            Address myAddress = new Address(cep,state,city,district,address,number);

            if(!TextUtils.isEmpty(complementET.getText())){
                String complement = complementET.getText().toString();
                myAddress.setComplement(complement);
            }

            usersRef
                    .child(uid)
                    .child("address")
                    .setValue(myAddress);

            Toast.makeText(this,"Cadastro finalizado, por favor, logue-se novamente.",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,SignInActivity.class));
            finish();

        }catch (EmptyFieldException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (LenghtException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private boolean thereAreEmptyFields(ArrayList<EditText> editTexts) {
        for(EditText thisEditText: editTexts){
            if(TextUtils.isEmpty(thisEditText.getText())){
                return true;
            }
        }
        return false;
    }
}
