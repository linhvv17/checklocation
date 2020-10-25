package com.example.locationchecker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationchecker.model.model.NotiDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class SOSActivity extends AppCompatActivity {

    Button button;

    Button stop;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

//    DatabaseReference check = database.getReference("Check");
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_o_s);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        ref = database.getReference().child("Kids").child(key);

        button = (Button)findViewById(R.id.SOS);

        stop= (Button)findViewById(R.id.Stop_SOS);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                ref.child("sos").setValue("true");
//                check.child(String.valueOf("check")).setValue(true);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                ref.child("sos").setValue("false");
            }
        });
    }
}