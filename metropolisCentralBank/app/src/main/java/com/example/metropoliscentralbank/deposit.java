package com.example.metropoliscentralbank;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class deposit extends AppCompatActivity {

    EditText amount;
    Button deposit;
    int totMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_amount);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("clientData/"+pinScreen.cnicString);

        amount = findViewById(R.id.givenAmount);
        deposit = findViewById(R.id.btnDeposit);


        deposit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String dateAndTime = dtf.format(now);

                int money = Integer.parseInt(amount.getText().toString());
                String hist = MainMenu.history + "Deposited " + amount.getText().toString() +"\n"+"("+ dateAndTime +")\n \n";
                totMoney = money + Integer.parseInt(MainMenu.balanceString);
                String m = String.valueOf(totMoney);
                HashMap<String,Object> hash = new HashMap<>();
                hash.put("clientBalance",m);
                hash.put("clientHistory",hist);

                myRef.updateChildren(hash).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Deposited",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(deposit.this, MainMenu.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

    }
}