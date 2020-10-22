package com.example.locationchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.locationchecker.activity.HomeParentsActivity;
import com.example.locationchecker.activity.TypeLoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
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