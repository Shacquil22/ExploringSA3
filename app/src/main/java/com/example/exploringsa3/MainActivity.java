package com.example.exploringsa3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize handler
        new Handler().postDelayed(() -> {

            //Redirect to main activity or landing screen
            startActivity(new Intent(MainActivity.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }, 3000);
    }
}