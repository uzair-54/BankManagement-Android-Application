package com.example.metropoliscentralbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class pinScreen extends AppCompatActivity {

    Button createAccount,enterPin;
    EditText cnic;
    Pinview pin;
    static String cnicString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_code_screen);

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        createAccount = findViewById(R.id.createAccountButton);
        enterPin = findViewById(R.id.enterButton);
        cnic = findViewById(R.id.cnicTextBox);
        pin = findViewById(R.id.pinOnlogIn);

        enterPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cnicString = cnic.getText().toString();
                boolean flag = true;

                if (cnicString.length() != 13) {
                    Toast.makeText(getApplicationContext(),"CNIC is not fully mentioned",Toast.LENGTH_LONG).show();
                    flag = false;
                }

                if (flag){

                    DatabaseReference myRef = database.getReference("clientData/"+cnicString);
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String clientPin = snapshot.child("clientPin").getValue().toString();
                            String givenPin = pin.getValue();
                            if (clientPin.equals(givenPin)){
                                Intent intent = new Intent(pinScreen.this,MainMenu.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),"Invalid pin or CNIC",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pinScreen.this,createAccount.class);
                startActivity(intent);
                finish();
            }
        });
    }
}