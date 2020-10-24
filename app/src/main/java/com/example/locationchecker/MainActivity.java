package com.example.locationchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.locationchecker.activity.HomeParentsActivity;
import com.example.locationchecker.activity.TypeLoginActivity;
import com.example.locationchecker.model.model.MapDTO;
import com.example.locationchecker.service.RestartServiceBroadcastReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MapDTO> mapDTOS;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Maps");

    DatabaseReference check = database.getReference("Check");


    private FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent myIntent = new Intent(this, TypeLoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(getApplicationContext());
        }
    }

    @Override
    protected void onStart() {
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user!=null){
//            Intent intent = new Intent(this, HomeParentsActivity.class);
//            startActivity(intent);
//        } else {
//
//        }

        super.onStart();
    }
}