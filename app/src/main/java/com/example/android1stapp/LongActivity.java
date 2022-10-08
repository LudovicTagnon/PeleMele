package com.example.android1stapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class LongActivity extends AppCompatActivity {

    private EditText inputText;
    private ProgressBar PB;
    private TacheLongue tacheLongue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long);

        inputText = (EditText) findViewById(R.id.inputTexte);

        this.PB = this.findViewById(R.id.progressBar);
        PB.setVisibility(ProgressBar.INVISIBLE);

        Button confirmerNombre = findViewById(R.id.buttonConfirmer);
        confirmerNombre.setOnClickListener((v) -> {
            Toast.makeText(LongActivity.this, "Tache Longue demaree ", Toast.LENGTH_SHORT).show();
            this.PB.setVisibility(ProgressBar.VISIBLE);
            this.tacheLongue = new TacheLongue(this, Integer.parseInt(inputText.getText().toString()));
            tacheLongue.start();
        });

    }


    public class TacheLongue extends Thread {

        protected LongActivity activite;
        protected ProgressBar PB;
        protected int input;

        TacheLongue(LongActivity a, int j) {
            this.activite = a;
            this.PB = a.findViewById(R.id.progressBar);
            PB.setMax(j + 1);
            this.input = j;
        }

        @Override
        public void run() {
            int nbnb1er = 0;
            if (!Thread.interrupted()) {
                nbnb1er = 0;
                for (int i = 2; i < this.input; ) {
                    PB.setProgress(i);
                    Log.i("LongActivity", i + "secondes");
                    int premier = 1;
                    for (int loop = 2; loop <= i; loop++) {
                        if ((i % loop) == 0 && loop != i) {
                            premier = 0;
                        }
                    }
                    if (premier != 0) {
                        nbnb1er++;
                    }
                    i++;
                }

                int finalNbnb1er = nbnb1er;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PB.setVisibility(View.INVISIBLE);
                        PB.setProgress(0);
                        Toast.makeText(LongActivity.this, "Fin de la tache longue", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LongActivity.this, "Nombre de nombres premiers avant input: " + finalNbnb1er, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
}