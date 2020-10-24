package com.example.locationchecker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationchecker.ExampleDialog;
import com.example.locationchecker.R;
import com.example.locationchecker.fragment.ParentCallFragment;
import com.example.locationchecker.fragment.ParentHomeFragment;
import com.example.locationchecker.fragment.ParentMessageFragment;
import com.example.locationchecker.fragment.ParentSettingFragment;
import com.example.locationchecker.fragment.RenderCodeAddKidFragment;
import com.example.locationchecker.fragment.TypeLoginFragment;
import com.example.locationchecker.model.Kid;
import com.example.locationchecker.model.Parent;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeParentsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String YOUR_FRAGMENT_STRING_TAG = "ABCD";
    private TextView tvName;
    private Button btnAdd;

    private FirebaseDatabase database;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference refUser;
    private FirebaseUser mUser;
    private GoogleApiClient mGoogleApiClient;

    final Fragment fragment1 = new ParentHomeFragment();
    final Fragment fragment2 = new ParentMessageFragment();
    final Fragment fragment3 = new ParentCallFragment();
    final Fragment fragment4 = new ParentSettingFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    private String android_id;
    private DatabaseReference userReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Parent parent = dataSnapshot.getValue(Parent.class);
                // ...
                if (parent!=null){
                    tvName.setText(parent.getName());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        userReference.addValueEventListener(postListener);

        btnAdd.setOnClickListener(this);


        fm.beginTransaction().add(R.id.frame_container, fragment4, "3").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.frame_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.frame_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.frame_container,fragment1, "1").commit();




        //
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    private void initView() {
        tvName = findViewById(R.id.tvName);
        btnAdd = findViewById(R.id.btnAdd);
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
            case R.id.btnAdd:
                Toast.makeText(this, "Add User", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, RenderCodeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}