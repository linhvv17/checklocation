package com.example.locationchecker.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locationchecker.ExampleDialog;
import com.example.locationchecker.R;
import com.example.locationchecker.Service;
import com.example.locationchecker.model.Kid;
import com.example.locationchecker.model.Parent;
import com.example.locationchecker.model.model.MapDTO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParentHomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

//    DatabaseReference check = database.getReference().child("Kids").child("565345");

    //
    private ArrayList<MapDTO> mapDTOS;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
//    DatabaseReference refMap = database.getReference().child("Maps");

    private FirebaseUser firebaseUser;
    private DatabaseReference kidReference;
    private DatabaseReference mapsReference;
    private DatabaseReference checkReference;
    private LatLng sydney;


    GoogleMap mMap;
    MapView mapView;
    private GoogleApiClient mGoogleApiClient;

    private FirebaseUser mUser;
    private DatabaseReference userReference;
    String text;

    public ParentHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_parent_home, container, false);




        mGoogleApiClient = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

//        mapDTOS = new ArrayList<>();

//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());

//        userReference.child("code");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Parent parent = snapshot.getValue(Parent.class);
                // ...
                if (parent!=null){
                    text = parent.getCode();

                    checkReference =  ref.child("Kids").child(text);

                    checkReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Get Post object and use the values to update the UI
                            Kid kid = snapshot.getValue(Kid.class);
                            // ...
                            if (kid != null) {
                                Boolean check = Boolean.valueOf(kid.getSos());
                                Log.d("check", String.valueOf(check));

                                if (check.equals(true)) {
//                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sos);
//                                    openDialog();

//                                    ExampleDialog exampleDialog = new ExampleDialog();
//                                    exampleDialog.setCode(text);
//                                    exampleDialog.show(getFragmentManager(), "example dialog");

                                } else {

                                    return;
                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        checkReference =  ref.child("Kids").child(text);
//
//        checkReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Get Post object and use the values to update the UI
//                Kid kid = snapshot.getValue(Kid.class);
//                // ...
//                if (kid != null) {
//                    Boolean check = Boolean.valueOf(kid.getSos());
//                    Log.d("check", String.valueOf(check));
//
//                    if (check.equals(true)) {
////                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sos);
//                        openDialog();
//                    } else {
//
//                        return;
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return view;
    }


    public void openDialog() {
            ExampleDialog exampleDialog = new ExampleDialog();
            exampleDialog.show(getFragmentManager(), "example dialog");
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapDTOS = new ArrayList<>();

        mapsReference = FirebaseDatabase.getInstance().getReference("KidMaps").child(text);
        MapsInitializer.initialize(getContext());
        this.mMap = googleMap;
        //bat la ban
        googleMap.getUiSettings().setCompassEnabled(true);
        //bam vao de ve vi tri cua minh
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
//        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setTrafficEnabled(true);
        googleMap.setBuildingsEnabled(true);

        // Lay du lieu tu firebase
//        kidReference = FirebaseDatabase.getInstance().getReference().child("Kids").child("332943");
//        getLocation();


        //kieu hien thi
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);

        // Lay du lieu tu firebase
//        kidReference = FirebaseDatabase.getInstance().getReference().child("Kids").child("565345");
//        kidReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                Kid kid = snapshot.getValue(Kid.class);
//                // ...
//                if (kid!= null){
//                    String lat = kid.getLat();
//                    String lng = kid.getLng();
//                    sydney = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
//                    mMap.addMarker(new MarkerOptions().position(sydney).title("My Son in here! \n "+lat+","+lng));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        // lay du lieu map
        mapsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){
                    MapDTO map = ds.getValue(MapDTO.class);
//                  ((ArrayList) mapDTOS ).add(map);
                    mapDTOS.add(map);

                }
//
                // Add a marker in Sydney and move the camera
                if (mapDTOS!=null){
                LatLng start = new LatLng(mapDTOS.get(0).getLatitude(), mapDTOS.get(0).getLongitude());
                LatLng end = new LatLng(mapDTOS.get(mapDTOS.size()-1).getLatitude(), mapDTOS.get(mapDTOS.size()-1).getLongitude());

                    mMap.addMarker(new MarkerOptions()
                            .position(end)
                            .title("Điểm kết thúc!"));
//                    .setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
//                );
                    for (int i = 0; i < mapDTOS.size() - 1; i++) {
                        MapDTO src = mapDTOS.get(i);
                        MapDTO dest = mapDTOS.get(i + 1);

                        // mMap is the Map Object
                        Polyline line = mMap.addPolyline(
                                new PolylineOptions().add(
                                        new LatLng(src.getLatitude(), src.getLongitude()),
                                        new LatLng(dest.getLatitude(),dest.getLongitude())
                                ).width(10).color(Color.BLUE).geodesic(true)
                        );
                    }
//
                    mMap.addMarker(new MarkerOptions().position(start).title("Điểm bắt đầu!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(end,18));
                }
//                LatLng start = new LatLng(mapDTOS.get(0).getLatitude(), mapDTOS.get(0).getLongitude());
//                LatLng end = new LatLng(mapDTOS.get(mapDTOS.size()-1).getLatitude(), mapDTOS.get(mapDTOS.size()-1).getLongitude());
                //tao 1 marker
//                mMap.addMarker(new MarkerOptions()
//                        .position(end)
//                        .title("Điểm kết thúc!"));
////                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
////                );
//                for (int i = 0; i < mapDTOS.size() - 1; i++) {
//                    MapDTO src = mapDTOS.get(i);
//                    MapDTO dest = mapDTOS.get(i + 1);
//
//                    // mMap is the Map Object
//                    Polyline line = mMap.addPolyline(
//                            new PolylineOptions().add(
//                                    new LatLng(src.getLatitude(), src.getLongitude()),
//                                    new LatLng(dest.getLatitude(),dest.getLongitude())
//                            ).width(10).color(Color.BLUE).geodesic(true)
//                    );
//                }
////
//                mMap.addMarker(new MarkerOptions().position(start).title("Điểm bắt đầu!"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(end,18));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        getLocation();
//        LatLng myLL = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in My Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            //            Common.checkAndRequestPermissionsGPS(getActivity());
        }

//        showMarkerToGoogleMap();

    }

//    private LatLng getLocation() {
//
//        ValueEventListener postListener = new ValueEventListener() {
//
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                Kid kid = dataSnapshot.getValue(Kid.class);
//                // ...
//                if (kid!= null){
//                    sydney = new LatLng(kid.getLat(), kid.getLng());
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        };
//        kidReference.addValueEventListener(postListener);
//        return sydney;
//    }

    public void showMarkerToGoogleMap(LatLng position) {
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions().position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_active));
        mMap.addMarker(markerOptions);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}