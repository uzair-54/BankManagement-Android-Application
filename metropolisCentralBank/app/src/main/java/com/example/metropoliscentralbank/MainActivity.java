package com.example.metropoliscentralbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
ProgressBar pb;
int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById (R.id.progressbar);

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mySuperIntent = new Intent(MainActivity.this, login.class);
                    startActivity(mySuperIntent);
                    finish();
                }
            }, 3500);

        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run()
            {
                counter ++;
                pb.setProgress(counter);

                if(counter == 100)
                    t.cancel();
            }
        };
        t.schedule(tt,0,35);

    }
}