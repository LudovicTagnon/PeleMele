package com.example.android1stapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MainActivity", "une info");

        Button bonjour = (Button) findViewById(R.id.buttonBonjour);
        bonjour.setOnClickListener((v) -> {
            Toast.makeText(MainActivity.this, "Bonjour", Toast.LENGTH_SHORT).show();
        });

        Button chrono = (Button) findViewById(R.id.buttonChrono);
        chrono.setOnClickListener((v) -> {
            Intent ic = new Intent(MainActivity.this, ChronometreActivity.class);
            startActivity(ic);
        });

        Button photo = (Button) findViewById(R.id.buttonPhoto);
        photo.setOnClickListener((v) -> {
            Intent ic = new Intent(MainActivity.this, PhotoActivity.class);
            startActivity(ic);
        });

        Button dernierePhoto = (Button) findViewById(R.id.buttonDernierePhoto);
        dernierePhoto.setOnClickListener((v) -> {
            FileInputStream fis = null;
            try {
                Toast.makeText(MainActivity.this, "Test affichage photo", Toast.LENGTH_SHORT).show();
                fis = openFileInput("image.data");
                Bitmap bm = BitmapFactory.decodeStream(fis);
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                iv.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Erreur affichage photo ", Toast.LENGTH_SHORT).show();
            }

        });

        Button calculLong = (Button) findViewById(R.id.buttonCalculLong);
        calculLong.setOnClickListener((v) -> {
            Intent ic = new Intent(MainActivity.this, LongActivity.class);
            startActivity(ic);
        });

        Button meteo = (Button) findViewById(R.id.buttonMeteo);
        meteo.setOnClickListener((v) -> {
            Intent ic = new Intent(MainActivity.this, MeteoActivity.class);
            startActivity(ic);
        });

        Button contacts = (Button) findViewById(R.id.buttonContacts);
        contacts.setOnClickListener((v) -> {
            Intent ic = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(ic);
        });

        Button accelerometre = (Button) findViewById(R.id.buttonAccelerometre);
        accelerometre.setOnClickListener((v) -> {
            Intent ic = new Intent(MainActivity.this, CapteursActivity.class);
            startActivity(ic);
        });

        Button select = (Button) findViewById(R.id.buttonSelect);
        select.setOnClickListener((v) -> {
            Intent ic = new Intent(MainActivity.this, SelectActivity.class);
            startActivity(ic);
        });

    }

    @Override
    //Error if protected (clash between origin function and override function)
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemQuitter) {
            finish();
            return true;
        } else if (id == R.id.itemChrono) {
            Intent ic = new Intent(MainActivity.this, ChronometreActivity.class);
            startActivity(ic);
            /*View viewChrono = findViewById(R.id.imageButtonStart);
            viewChrono.performClick();*/
            return true;
        } else {
            return false;
        }

    }

}