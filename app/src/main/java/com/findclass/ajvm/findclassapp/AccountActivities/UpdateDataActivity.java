package com.findclass.ajvm.findclassapp.AccountActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Exception.EmptyFieldException;
import com.findclass.ajvm.findclassapp.Exception.PhoneLenghtException;
import com.findclass.ajvm.findclassapp.Model.Address;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.findclass.ajvm.findclassapp.menuActivities.MenuProfessorActivity;
import com.findclass.ajvm.findclassapp.menuActivities.MenuAlunoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UpdateDataActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference usersRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        //setando os atributos;
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        currentUser = auth.getCurrentUser();

        //setar os dados do bd na tela;
        usersRef
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //recuperar dados do banco de dados;
                                User user = dataSnapshot.getValue(User.class);
                                Address userAddress = user.getAddress();

                                //setar valores do banco nos elementos da activities;
                                ((TextView) findViewById(R.id.nameEditText)).setText(user.getName());
                                ((TextView) findViewById(R.id.surnameEditText)).setText(user.getSurname());
                                ((TextView) findViewById(R.id.phoneEditText)).setText(user.getTelephony());

                                ((TextView) findViewById(R.id.cepEditText)).setText(userAddress.getCep());
                                ((TextView) findViewById(R.id.stateEditText)).setText(userAddress.getState());
                                ((TextView) findViewById(R.id.cityEditText)).setText(userAddress.getCity());
                                ((TextView) findViewById(R.id.districtEditText)).setText(userAddress.getDistrict());
                                ((TextView) findViewById(R.id.addressEditText)).setText(userAddress.getAddress());
                                ((TextView) findViewById(R.id.numberEditText)).setText(String.valueOf(userAddress.getNumber()));
                                ((TextView) findViewById(R.id.complementEditText)).setText(userAddress.getComplement());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Erro
                            }
                        }
                );

    }

    public void finishUpdateData(View view) throws EmptyFieldException, PhoneLenghtException {
        //progressdialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Alterando dados...");
        progressDialog.show();

        //recuperar elementos dos EditText da tela;
        EditText name = findViewById(R.id.nameEditText);
        EditText surname = findViewById(R.id.surnameEditText);
        EditText phone = findViewById(R.id.phoneEditText);

        EditText cep = findViewById(R.id.cepEditText);
        EditText state = findViewById(R.id.stateEditText);
        EditText city = findViewById(R.id.cityEditText);
        EditText district = findViewById(R.id.districtEditText);
        EditText address = findViewById(R.id.addressEditText);
        EditText number = findViewById(R.id.numberEditText);
        EditText complement = findViewById(R.id.complementEditText);

        //arraylist para validação;
        ArrayList<EditText> editTexts = new ArrayList<>();
        editTexts.add(name);
        editTexts.add(surname);
        editTexts.add(phone);
        editTexts.add(cep);
        editTexts.add(state);
        editTexts.add(city);
        editTexts.add(district);
        editTexts.add(address);
        editTexts.add(number);

        try{
            //Minhas validações;
            if (thereAreEmptyFields(editTexts)) {
                throw new EmptyFieldException();
            } else if (phone.getText().length() < 14) {
                throw new PhoneLenghtException();
            } else {
                //recupero a referencia para esse usuário no banco de dados;
                DatabaseReference thisUserRef = usersRef.child(auth.getCurrentUser().getUid());

                //setar as alterações no usuário
                thisUserRef.child("name").setValue(name.getText().toString());
                thisUserRef.child("surname").setValue(surname.getText().toString());
                thisUserRef.child("telephony").setValue(phone.getText().toString());

                //criar objeto de endereço desse usuário alterado;
                Address thisUserAddress = new Address(
                        cep.getText().toString(),
                        state.getText().toString(),
                        city.getText().toString(),
                        district.getText().toString(),
                        address.getText().toString(),
                        Integer.valueOf(number.getText().toString()),
                        complement.getText().toString()
                );

                //recuperar referência para o endereço desse usuário;
                DatabaseReference thisUserAddressRef = thisUserRef.child("address");

                //setar o novo endereço do usuário;
                thisUserAddressRef.setValue(thisUserAddress);

                progressDialog.dismiss();
                Toast.makeText(this, "Dados alterados com sucesso.", Toast.LENGTH_LONG).show();

                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if((dataSnapshot.child(auth.getCurrentUser().getUid()).child("professor").getValue(String.class).toString()).equals("true")){
                                startActivity(new Intent(UpdateDataActivity.this,MenuProfessorActivity.class));
                            }else {
                                progressDialog.dismiss();
                                startActivity(new Intent(UpdateDataActivity.this,MenuAlunoActivity.class));
                            }
                        }catch (Exception e){
                            Toast.makeText(UpdateDataActivity.this, "Problemas no sistema, você será deslogado.", Toast.LENGTH_LONG).show();
                            auth.signOut();

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //erro
                    }
                });
            }
            //exceções;
        } catch (EmptyFieldException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (PhoneLenghtException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();
    }

    private boolean thereAreEmptyFields(ArrayList<EditText> editTexts) {
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText())) {
                return true;
            }
        }
        return false;
    }
}
