package com.example.metropoliscentralbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class transfer extends AppCompatActivity {

    EditText cnicNum;
    Button transfer;
    static String reBalance,reCnic,reHist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_amount);

        cnicNum = findViewById(R.id.cnicTransferBox);
        transfer = findViewById(R.id.btnTransfer);

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reCnic = cnicNum.getText().toString();

                if (reCnic.equals(pinScreen.cnicString) || reCnic.length() != 13){
                    Toast.makeText(getApplicationContext(),"Incorrect CNIC",Toast.LENGTH_LONG).show();
                }
                else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference recivRef = database.getReference("clientData/" + reCnic);

                    recivRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("clientBalance")) {
                                reBalance = snapshot.child("clientBalance").getValue().toString();
                                reHist = snapshot.child("clientHistory").getValue().toString();
                                Intent intent = new Intent(transfer.this, transferPart2.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect CNIC", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
}