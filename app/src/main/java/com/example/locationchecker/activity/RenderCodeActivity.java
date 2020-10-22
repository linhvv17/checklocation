package com.example.locationchecker.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.locationchecker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class RenderCodeActivity extends AppCompatActivity {
    private TextView tvCode;
    private String code;
    private String android_id;
    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    private String firebaseUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_code);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        initView();
        randomCode();
        mAuth = FirebaseAuth.getInstance();
        firebaseUserId = mAuth.getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUserId);
        HashMap<String, Object> parent = new HashMap<>();
        parent.put("code",code) ;
        //parent.put("phone",edtPhone.getText().toString());
        userReference.updateChildren(parent);

        tvCode.setText(code);
    }

    private void randomCode() {
        Random rd = new Random();
        int myValue = rd.nextInt(999999-100000)+100000;
        code = String.valueOf(myValue);
    }

    private void initView() {
        tvCode = findViewById(R.id.tv_code);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}