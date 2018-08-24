package com.findclass.ajvm.findclassapp.AccountActivities;

import android.app.ProgressDialog;
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
    //Elementos da Activity;
    private Button finishButton;
    private EditText cepET;
    private EditText stateET;
    private EditText cityET;
    private EditText districtET;
    private EditText addressET;
    private EditText numberET;
    private EditText complementET;

    //Referências do Firebase
    private DatabaseReference usersRef;
    private FirebaseAuth auth;

    //Uid do usuário atual;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step3);

        //Situando o usuário;
        Toast.makeText(this, "Insira seu endereço.", Toast.LENGTH_LONG).show();

        //Setando os elementos da Activity;
        finishButton = findViewById(R.id.buttonFinish);
        cepET = findViewById(R.id.editTextCep);
        stateET = findViewById(R.id.editTextState);
        cityET = findViewById(R.id.editTextCity);
        districtET = findViewById(R.id.editTextDistrict);
        addressET = findViewById(R.id.editTextAddress);
        numberET = findViewById(R.id.editTextNumber);
        complementET = findViewById(R.id.editTextComplement);

        //setando os elementos do Firebase
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        auth = FirebaseAuth.getInstance();

        //setando o Uid do usuário atual;
        uid = auth.getCurrentUser().getUid();

        //deslogando o usuário para evitar problemas de conexão com o firebase;
        auth.signOut();

        //Botão de conclusão de cadastro;
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
        //progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Finalizando cadastro...");
        progressDialog.show();

        try {
            //Arraylista para validação;
            ArrayList<EditText> editTexts = new ArrayList<>();
            editTexts.add(cepET);
            editTexts.add(stateET);
            editTexts.add(cityET);
            editTexts.add(districtET);
            editTexts.add(addressET);
            editTexts.add(numberET);

            //validação de campos vazios;
            if(thereAreEmptyFields(editTexts)){
                throw new EmptyFieldException();
            }

            //validação de cep;
            String cep = cepET.getText().toString();
            if(cep.length() != 9){
                throw new LenghtException();
            }

            //recuperação de dados nos elementos da activity;
            String state = stateET.getText().toString();
            String city = cityET.getText().toString();
            String district = districtET.getText().toString();
            String address = addressET.getText().toString();
            int number = Integer.valueOf(numberET.getText().toString());

            //criação do objeto de endereço;
            Address myAddress = new Address(cep,state,city,district,address,number);

            //verificação para saber se o endereço possui ou não, complemento;
            if(!TextUtils.isEmpty(complementET.getText())){
                String complement = complementET.getText().toString();
                myAddress.setComplement(complement);
            }

            //salvar endereço no banco;
            usersRef
                    .child(uid)
                    .child("address")
                    .setValue(myAddress);

            //inicio a activity;
            progressDialog.dismiss();
            Toast.makeText(this,"Cadastro finalizado, por favor, logue-se novamente.",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,SignInActivity.class));
            finish();
            //tratamento de exceções;
        }
        catch (EmptyFieldException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (LenghtException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        //quando o usuário precionar voltar, ele será redirecionado à tela de login;
        try {
            auth.signOut();
        }catch (Exception e){
            //Does not need any other action.
        }
        startActivity(new Intent(this,SignInActivity.class));
        finish();
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
