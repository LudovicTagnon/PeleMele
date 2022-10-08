package com.example.android1stapp;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import java.time.LocalDateTime;

public class ChronometreActivity extends AppCompatActivity {

    private LocalDateTime ldt_start;
    private LocalDateTime ldt_stop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometre);


        Toast.makeText(ChronometreActivity.this, "heure", Toast.LENGTH_SHORT).show();

        ImageButton Start = (ImageButton) findViewById(R.id.imageButtonStart);
        ImageButton Stop = (ImageButton) findViewById(R.id.imageButtonStop);

        Stop.setEnabled(false);

        Start.setOnClickListener((v) -> {



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.ldt_start = LocalDateTime.now();
                Toast.makeText(ChronometreActivity.this, "Start time: " + ldt_start.getSecond(), Toast.LENGTH_SHORT).show();
            }
            Stop.setEnabled(true);
            Start.setEnabled(false);
        });

        Stop.setOnClickListener((v) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.ldt_stop = LocalDateTime.now();
                Toast.makeText(ChronometreActivity.this, "Stop time: " + ((int) ldt_stop.getSecond()-ldt_start.getSecond()) + " seconds", Toast.LENGTH_SHORT).show();
            }
            Stop.setEnabled(false);
            Start.setEnabled(true);
        });

    }
}