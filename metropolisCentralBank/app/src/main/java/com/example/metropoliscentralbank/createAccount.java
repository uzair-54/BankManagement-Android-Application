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
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class createAccount extends AppCompatActivity {

    EditText name,age,cnic,phnNum,iniBal;
    Pinview pinNum;
    Button btn;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("clientData");

        name = findViewById(R.id.nameTextbox);
        age = findViewById(R.id.textboxAge);
        cnic = findViewById(R.id.cnic);
        phnNum = findViewById(R.id.phoneNumber);
        iniBal = findViewById(R.id.initialDeposit);
        pinNum = findViewById(R.id.pinBox);
        btn = findViewById(R.id.createAccountButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String c_name = name.getText().toString();
                String c_cnic = cnic.getText().toString();
                String c_phn = phnNum.getText().toString();
                String c_age = age.getText().toString();
                String c_balance = iniBal.getText().toString();
                String c_pin = pinNum.getValue();
                String c_email = mFirebaseAuth.getCurrentUser().getEmail();

                int c_balanceINT = Integer.parseInt(iniBal.getText().toString());
                boolean flag = true;

                if (c_balanceINT < 1500){
                    Toast.makeText(getApplicationContext(),"Insufficient Balance",Toast.LENGTH_LONG).show();
                    flag = false;
                }
                if ((c_name.equals(null)) || (c_cnic.equals(null)) || (c_phn.equals(null)) || (c_age.equals(null)) || (c_balance.equals(null)) || c_pin.equals(null)){
                    Toast.makeText(getApplicationContext(),"Something is not filled",Toast.LENGTH_LONG).show();
                    flag = false;
                }
                if (c_cnic.length() != 13) {
                    Toast.makeText(getApplicationContext(),"CNIC is not fully mentioned",Toast.LENGTH_LONG).show();
                    flag = false;
                }
                if (c_pin.length() != 4) {
                    Toast.makeText(getApplicationContext(),"PIN is not fully mentioned",Toast.LENGTH_LONG).show();
                    flag = false;
                }

                if (flag) {

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String dateAndTime = dtf.format(now);

                    String hist = "Initial Balance Deposited " + c_balance +"\n"+"("+dateAndTime + ")\n \n";
                    clientDetails data = new clientDetails (c_name,c_age,c_phn,c_email,c_balanceINT,c_pin,hist);
                    myRef.child(c_cnic).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Account created",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(createAccount.this,pinScreen.class);
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