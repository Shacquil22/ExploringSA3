package com.example.exploringsa3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exploringsa3.Favourites.FavouriteAdapter;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    private ImageButton btnBack;
    private Switch swUnits;
    private TextView txtDistance;
    private ImageButton btnCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack = findViewById(R.id.fpBtnBack);
        btnCategory = findViewById(R.id.btnCategory);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileFragment.class);
                startActivity(intent);
            }
        });

        swUnits = findViewById(R.id.units);
        txtDistance = findViewById(R.id.distance_units);

        registerForContextMenu(btnCategory);

        swUnits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(swUnits.isChecked()){

                    SharedPreferences userDetails = getApplicationContext().getSharedPreferences("distance", MODE_PRIVATE);
                    SharedPreferences.Editor edit = userDetails.edit();
                    edit.clear();
                    edit.putString("Units", "True");
                    edit.apply();

                }
                else {

                    SharedPreferences userDetails = getApplicationContext().getSharedPreferences("distance", MODE_PRIVATE);
                    SharedPreferences.Editor edit = userDetails.edit();
                    edit.clear();
                    edit.putString("Units", "False");
                    edit.apply();
                }

            }
        });

    }

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_filter,menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        String selectedCategory;

        switch (item.getItemId()) {
            case R.id.popular:
                selectedCategory = "Popular";
                sharedPreferences = getSharedPreferences("SelectedCategory", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SelectedCategory",selectedCategory);
                editor.apply();
                return true;
            case R.id.historical:
                selectedCategory = "Historical";
                sharedPreferences = getSharedPreferences("SelectedCategory", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorH = sharedPreferences.edit();
                editorH.putString("SelectedCategory",selectedCategory);
                editorH.apply();
                return true;
            case R.id.modern:
                selectedCategory = "Modern";
                sharedPreferences = getSharedPreferences("SelectedCategory", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorM = sharedPreferences.edit();
                editorM.putString("SelectedCategory",selectedCategory);
                editorM.apply();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}