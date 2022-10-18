package com.example.exploringsa3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            //Nelson Mandela Square
            LatLng NMS = new LatLng(-26.1071068951935, 28.054779024092685);
            googleMap.addMarker(new MarkerOptions().position(NMS).title("Nelson Mandela Square")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.nelson_mandela_square)));

            //Voortrekker Monument
            LatLng VM = new LatLng(-25.773507612395356, 28.175734579768815);
            googleMap.addMarker(new MarkerOptions().position(VM).title("Voortrekker Monument")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.voortrekker_monument)));

            //Union Buildings
            LatLng UB = new LatLng(-25.740181494018834, 28.21194874444055);
            googleMap.addMarker(new MarkerOptions().position(UB).title("Union Buildings")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.union_buildings)));

            //Hector Pieterson Memorial
            LatLng HPM = new LatLng(-26.235216372874984, 27.908007772536155);
            googleMap.addMarker(new MarkerOptions().position(HPM).title("Hector Pieterson Memorial")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hector_pieterson_memorial)));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NMS, 10));

            //if marker clicked Zoom

            //Types of maps
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    };

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable icon = ContextCompat.getDrawable(context,vectorResId);
        icon.setBounds(0,0,icon.getIntrinsicWidth(),icon.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(),icon.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        icon.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}