package com.example.locationchecker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.locationchecker.R;
import com.example.locationchecker.SOSActivity;
import com.example.locationchecker.ServiceAppKid;
import com.example.locationchecker.fragment.KidCallFragment;
import com.example.locationchecker.fragment.KidHomeFragment;
import com.example.locationchecker.fragment.KidMessageFragment;
import com.example.locationchecker.fragment.KidSettingFragment;
import com.example.locationchecker.model.Kid;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeKidsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String YOUR_FRAGMENT_STRING_TAG = "ABCD";
    private TextView tvName;
    private Button btnSOS;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference refUser;
    private FirebaseUser mUser;
    private GoogleApiClient mGoogleApiClient;

    final Fragment fragment1 = new KidHomeFragment();
    final Fragment fragment2 = new KidMessageFragment();
    final Fragment fragment3 = new KidCallFragment();
    final Fragment fragment4 = new KidSettingFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    private String android_id;
    private DatabaseReference userReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_kids);
        tvName = findViewById(R.id.tvNameKid);
        initView();
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        Log.d("MESSAGE",message);


//        android_id = Settings.Secure.getString(this.getContentResolver(),
//                Settings.Secure.ANDROID_ID);

        DatabaseReference kidReference = FirebaseDatabase.getInstance().getReference().child("Kids").child(message);


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Kid kid = dataSnapshot.getValue(Kid.class);
                // ...
                if (kid!=null){
                    tvName.setText(kid.getName());
                    String codeKid = kid.getCode();
                    Intent mIntent = new Intent(getApplicationContext(), ServiceAppKid.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("kidto", codeKid);
                    mIntent.putExtras(mBundle);
                    startService(mIntent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        kidReference.addValueEventListener(postListener);

//        btnAdd.setOnClickListener(this);


        fm.beginTransaction().add(R.id.frame_container, fragment4, "3").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.frame_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.frame_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.frame_container,fragment1, "1").commit();


        //
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeKidsActivity.this, SOSActivity.class);
                i.putExtra("key","146194");
                startActivity(i);
            }
        });
    }

    private void initView() {
//        tvName = findViewById(R.id.tvName);
        btnSOS = findViewById(R.id.imgSOS);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.navigation_mess:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.navigation_call:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.navigation_set:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgSOS:
//                Toast.makeText(this, "Add User", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, SOSActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}