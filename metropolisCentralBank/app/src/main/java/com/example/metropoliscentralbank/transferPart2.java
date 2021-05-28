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

public class transferPart2 extends AppCompatActivity {

    EditText amount;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_part2);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        amount = findViewById(R.id.transferAmount);
        btn = findViewById(R.id.btnTransferPart2);


        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String sendingMoney = amount.getText().toString();
                int sendingMoneyInt = Integer.parseInt(sendingMoney);

                int recivCurMoney = Integer.parseInt(transfer.reBalance);
                int totalRecivMoney = recivCurMoney + sendingMoneyInt;

                int senderCurMoney = Integer.parseInt(MainMenu.balanceString);
                int senderTotMoney = senderCurMoney - sendingMoneyInt;


                if (senderCurMoney < sendingMoneyInt){
                    Toast.makeText(getApplicationContext(),"Now enough funds",Toast.LENGTH_LONG).show();
                }
                else {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String dateAndTime = dtf.format(now);
                    String seHist = MainMenu.history + "Sent " + sendingMoney +"\n"+"("+ dateAndTime +")\n \n";
                    HashMap<String, Object> senderHash = new HashMap<>();
                    senderHash.put("clientBalance", String.valueOf(senderTotMoney));
                    senderHash.put("clientHistory",seHist);

                    DatabaseReference senderRef = database.getReference("clientData/" + pinScreen.cnicString);
                    senderRef.updateChildren(senderHash);

                    HashMap<String, Object> reciverHash = new HashMap<>();
                    String reHist = transfer.reHist + "Recieved " + sendingMoney +"\n"+"("+ dateAndTime +")\n \n";
                    reciverHash.put("clientBalance", String.valueOf(totalRecivMoney));
                    reciverHash.put("clientHistory",reHist);
                    DatabaseReference reciverRef = database.getReference("clientData/" + transfer.reCnic);
                    reciverRef.updateChildren(reciverHash).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Transfer successful",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(transferPart2.this, MainMenu.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}