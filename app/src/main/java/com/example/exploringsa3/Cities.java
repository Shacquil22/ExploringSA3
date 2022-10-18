package com.example.exploringsa3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Cities extends AppCompatActivity {

    private CardView pretoria;
    private CardView capeTown;
    private String city;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        pretoria = findViewById(R.id.cdvPretoria);
        capeTown = findViewById(R.id.cdvCapeTown);

        pretoria.setOnClickListener(v -> {
            city = "Pretoria";

            Intent i = new Intent(this,Landmarks.class);

            sharedPreferences = getSharedPreferences("city", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("City",city);
            editor.apply();

            startActivity(i);
        });

        capeTown.setOnClickListener(v -> {
            city = "Cape Town";

            Intent i = new Intent(this,Landmarks.class);

            sharedPreferences = getSharedPreferences("SelectedCity", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("City",city);
            editor.apply();

            startActivity(i);
        });
    }
}