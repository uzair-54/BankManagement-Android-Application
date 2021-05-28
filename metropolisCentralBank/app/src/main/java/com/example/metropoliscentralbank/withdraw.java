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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class withdraw extends AppCompatActivity {

    EditText amount;
    Button withdrawbtn;
    int totMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_amount);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("clientData/"+pinScreen.cnicString);

        amount = findViewById(R.id.withdrawAmnt);
        withdrawbtn = findViewById(R.id.withdrawbtn);

        withdrawbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int clientMoney = Integer.parseInt(MainMenu.balanceString);

                int money = Integer.parseInt(amount.getText().toString());

                if (money > clientMoney){
                    Toast.makeText(getApplicationContext(),"Invalid funds",Toast.LENGTH_LONG);
                }
                else {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String dateAndTime = dtf.format(now);


                    totMoney = clientMoney - money;
                    String m = String.valueOf(totMoney);
                    String hist = MainMenu.history + "Withdrawn " + amount.getText().toString() +"\n"+"("+ dateAndTime +")\n \n";
                    HashMap<String, Object> hash = new HashMap<>();
                    hash.put("clientBalance", m);
                    hash.put("clientHistory",hist);

                    myRef.updateChildren(hash).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Money withdrawn",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(withdraw.this, MainMenu.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });
    }
}