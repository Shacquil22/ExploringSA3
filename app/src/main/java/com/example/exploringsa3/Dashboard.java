package com.example.exploringsa3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //        Toolbar toolbar = findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);

//        DrawerLayout drawerLayout = findViewById(R.id.drawerL);
//        NavigationView navView = findViewById(R.id.navigation_view);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationBar);

//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
//                R.string.drawer_open, R.string.drawer_close);
//        //noinspection deprecation
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();

//        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu2);

//        navView.setNavigationItemSelectedListener(item -> {
//            if(item.getItemId() == R.id.SettingsPage) {
//                Toast.makeText(Dashboard.this, "Settings", Toast.LENGTH_SHORT).show();
//            } else if(item.getItemId() == R.id.LogoutUser) {
//                Toast.makeText(Dashboard.this, "Logout", Toast.LENGTH_SHORT).show();
//            }
//
//            return true;
//        });

        replaceFragment(new HomeFragment());

        bottomNav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.homeFragment:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.favouriteFragment:
                    replaceFragment(new FavouriteFragment());
                    break;
                case R.id.mapsFragment:
                    replaceFragment(new MapsFragment());
                    break;
                case R.id.profileFragment:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();
    }
}