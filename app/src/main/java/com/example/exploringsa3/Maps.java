package com.example.exploringsa3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exploringsa3.models.PlaceInfo;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.SphericalUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.checkerframework.checker.units.qual.m;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {

    private boolean isPermissionGranted;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationClient;
    private final int GPS_REQUEST_CODE = 9001;
    private ImageButton imgType;

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71,136));
    private AutoCompleteTextView mSearchText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;

    private ImageView mInfo;
    private ImageView mPlacePicker;

    private FloatingActionButton btnLocation;
    private ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentView);
        supportMapFragment.getMapAsync(this);

        checkMyPermission();

        FloatingActionButton fabLocation = findViewById(R.id.fabMyLocation);
        imgType = findViewById(R.id.ibMapTypes);
        mSearchText = findViewById(R.id.input_search);
        mInfo = findViewById(R.id.place_info);
        mPlacePicker = findViewById(R.id.place_picker);
        btnLocation = findViewById(R.id.fabMyLocation);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(view -> {
            geoLocate();
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurLoc();
            }
        });

        initMap();
        //init();

        mLocationClient = new FusedLocationProviderClient(this);

        fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurLoc();
                if (ActivityCompat.checkSelfPermission(Maps.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(Maps.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

            }
        });

        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(Maps.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e("TAG", "onClick: GooglePlayServicesRepairableException: " + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e("TAG", "onClick: GooglePlayServicesNotAvailableException: " + e.getMessage());
                }
            }
        });

        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: clicked place info");
                try{

                    if(mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    }else{
                        Log.d("TAG", "onClick: place info: " + mPlace.toString());
                        mMarker.showInfoWindow();
                    }

                } catch(NullPointerException e){
                    Log.e("TAG", "onClick: NullPointerException: " + e.getMessage());
                }
            }
        });

        registerForContextMenu(imgType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == PLACE_PICKER_REQUEST){
            Place place = PlacePicker.getPlace(this,data);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient,place.getId());

        } else if(requestCode == GPS_REQUEST_CODE) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(providerEnable) {
                Toast.makeText(Maps.this, "GPS is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Maps.this, "GPS is not enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_types, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mapDefault:
                Toast.makeText(this, "Default", Toast.LENGTH_SHORT).show();
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                imgType.setImageResource(R.drawable.bursts);
                return true;
            case R.id.mapSatellite:
                Toast.makeText(this, "Satellite", Toast.LENGTH_SHORT).show();
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                imgType.setImageResource(R.drawable.bursts_white);
                return true;
            case R.id.mapTerrain:
                Toast.makeText(this, "Terrain", Toast.LENGTH_SHORT).show();
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                imgType.setImageResource(R.drawable.bursts);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurLoc() {
        Log.d("TAG", "getDeviceLocation: getting the devices current location");
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            @SuppressLint("MissingPermission") Task location = mLocationClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Log.d("TAG", "onComplete: found location");
                        Location currentLocation = (Location) task.getResult();

                        Intent i = new Intent();

                        String mark = getIntent().getStringExtra("title");

                        LatLng loc = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                        int speed = 75;

                        switch (mark){
                            case "Nelson Mandela Square":
                                //Nelson Mandela Square
                                LatLng NMS = new LatLng(-26.1071068951935, 28.054779024092685);

                                float distance = Math.round(SphericalUtil.computeDistanceBetween(loc, NMS) / 1000);

                                float time =  Math.round((distance/speed)*60);

                                SharedPreferences sharedPreferences = getSharedPreferences("distance", Context.MODE_PRIVATE);

                                String units = sharedPreferences.getString("Units", "True");

                                if(units.equals("True")){

                                    //double m = 0.6;
                                    double miles = (distance * 0.6);

                                    Toast.makeText(Maps.this, miles + " m" + "\nDuration: " + time +" min", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Maps.this, distance + " km" + "\nDuration: " + time +" min", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            case"Voortrekker Monument":
                                //Voortrekker Monument
                                LatLng VM = new LatLng(-25.773507612395356, 28.175734579768815);

                                float distanceVM = Math.round(SphericalUtil.computeDistanceBetween(loc, VM) / 1000);

                                float timeVM =  Math.round((distanceVM/speed)*60);

                                SharedPreferences sharedPreferencesVM = getSharedPreferences("distance", Context.MODE_PRIVATE);

                                String unitsVM = sharedPreferencesVM.getString("Units", "True");

                                if(unitsVM.equals("True")){

                                    //double m = 0.6;
                                    double miles = (distanceVM * 0.6);

                                    Toast.makeText(Maps.this, miles + " m" + "\nDuration: " + timeVM +" min", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Maps.this, distanceVM + " km" + "\nDuration: " + timeVM +" min", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            case"Union Buildings":
                                //Union Buildings
                                LatLng UB = new LatLng(-25.740181494018834, 28.21194874444055);

                                float distanceUB = Math.round(SphericalUtil.computeDistanceBetween(loc, UB) / 1000);

                                float timeUB =  Math.round((distanceUB/speed)*60);

                                SharedPreferences sharedPreferencesUB = getSharedPreferences("distance", Context.MODE_PRIVATE);

                                String unitsUB = sharedPreferencesUB.getString("Units", "True");

                                if(unitsUB.equals("True")){

                                    //double m = 0.6;
                                    double miles = (distanceUB * 0.6);

                                    Toast.makeText(Maps.this, miles + " m" + "\nDuration: " + timeUB +" min", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Maps.this, distanceUB + " km" + "\nDuration: " + timeUB +" min", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case"Hector Pieterson Memorial":
                                //Hector Pieterson Memorial
                                LatLng HPM = new LatLng(-26.235216372874984, 27.908007772536155);

                                float distanceHPM = Math.round(SphericalUtil.computeDistanceBetween(loc, HPM) / 1000);

                                float timeHPM =  Math.round((distanceHPM/speed)*60);

                                SharedPreferences sharedPreferencesHPM = getSharedPreferences("distance", Context.MODE_PRIVATE);

                                String unitsHPM = sharedPreferencesHPM.getString("Units", "True");

                                if(unitsHPM.equals("True")){

                                    //double m = 0.6;
                                    double miles = (distanceHPM * 0.6);

                                    Toast.makeText(Maps.this, miles + " m" + "\nDuration: " + timeHPM +" min", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Maps.this, distanceHPM + " km" + "\nDuration: " + timeHPM +" min", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }

                        moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),16,"My Location");
                    } else {
                        Log.d("TAG", "onComplete: current location not found");
                        Toast.makeText(Maps.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (SecurityException e){
            Log.d("TAG", "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng,float zoom, PlaceInfo placeInfo){
        Log.d("TAG", "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        mGoogleMap.clear();

        mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Maps.this));

        if(placeInfo != null){
            try{

                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);

                mMarker = mGoogleMap.addMarker(options);
                mGoogleMap.addMarker(options);

            }catch(NullPointerException e){
                Log.e("TAG", "moveCamera: NullPointerException: " + e.getMessage());
            }
        } else{
            mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        }

    }

    private void moveCamera(LatLng latLng,float zoom,String title){
        Log.d("TAG", "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title(title);
            mGoogleMap.addMarker(options);
        }
    }

    private void init(){
        Log.d("TAG","init: Initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    //Method for searching
                    geoLocate();
                }
                return false;
            }
        });
    }

    private void geoLocate() {
        Log.d("TAG", "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(Maps.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString,1);
        } catch(IOException e){
            Log.d("TAG", "geoLocate: IOException: " + e.getMessage());
        }

        if(list.size() > 0) {
            Address address = list.get(0);

            Log.d("TAG", "geoLocate: found a location: " + address.toString());
            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),15,address.getAddressLine(0));

            mInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PlaceInfo info = new PlaceInfo();

                    info.setName(address.getFeatureName());
                    info.setPhoneNumber(address.getPhone());
                    info.setAddress(address.getAddressLine(0));

                    Toast.makeText(Maps.this, "Name: " + info.getName()
                            + "\nPhone Number: " + info.getPhoneNumber()
                            + "\nWebsite: " + info.getAddress(), Toast.LENGTH_SHORT).show();


                }
            });
        }
    }

    private void initMap() {
        if(isPermissionGranted) {
            if(isGPEnable()) {
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentView);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(this);
            }
        }
    }

    private boolean isGPEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(providerEnable) {
            return true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS is required for this app tp work. Please enable GPS")
                    .setPositiveButton("Yes",((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        //noinspection deprecation
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                    }))
                    .setCancelable(false)
                    .show();
        }
        return false;
    }

    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(Maps.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }

        }).check();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;

        init();

//        LatLng Home = new LatLng(-25.857125891399228, 28.18676226132291);
//        googleMap.addMarker(new MarkerOptions().position(Home).title("Marker at Home"));
//
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Home, 16));
        //Types of maps
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Intent i = new Intent();

        String mark = getIntent().getStringExtra("title");

        switch (mark){
            case "Nelson Mandela Square":
                //Nelson Mandela Square
                LatLng NMS = new LatLng(-26.1071068951935, 28.054779024092685);
                googleMap.addMarker(new MarkerOptions().position(NMS).title("Nelson Mandela Square"));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NMS, 16));
                //Types of maps
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case"Voortrekker Monument":
                //Voortrekker Monument
                LatLng VM = new LatLng(-25.773507612395356, 28.175734579768815);
                googleMap.addMarker(new MarkerOptions().position(VM).title("Voortrekker Monument"));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VM, 16));
                //Types of maps
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case"Union Buildings":
                //Union Buildings
                LatLng UB = new LatLng(-25.740181494018834, 28.21194874444055);
                googleMap.addMarker(new MarkerOptions().position(UB).title("Union Buildings")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.union_buildings)));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UB, 16));
                //Types of maps
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case"Hector Pieterson Memorial":
                //Hector Pieterson Memorial
                LatLng HPM = new LatLng(-26.235216372874984, 27.908007772536155);
                googleMap.addMarker(new MarkerOptions().position(HPM).title("Hector Pieterson Memorial")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.hector_pieterson_memorial)));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HPM, 16));
                //Types of maps
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }

    }

}