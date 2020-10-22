package com.example.locationchecker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.locationchecker.R;
import com.example.locationchecker.model.Kid;
import com.example.locationchecker.model.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class KidSettingFragment extends Fragment {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private EditText edtName, edtPhone;
    private Button btnUpdate;

    private FirebaseUser firebaseUser;
    private DatabaseReference userReference;
    private String android_id ;
    private String message;

    public KidSettingFragment() {
        // Required empty public constructor
    }

    private void initView(View view){
        edtName = view.findViewById(R.id.kid_name);
        edtPhone = view.findViewById(R.id.kid_phone);
        btnUpdate = view.findViewById(R.id.btn_doi_info);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_kid_setting, container, false);
        //
//        if (getArguments() != null) {
////            String strtext = getArguments().getString("message");
            Intent intent = getActivity().getIntent();
            message = intent.getStringExtra("message");
            Log.d("CODE RECEIVER:",""+message);
//        }
//        String code = getArguments().getString("done");

        initView(view);
//        android_id = Settings.Secure.getString(getContext().getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//        //
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference().child("Kids").child(message);
        getInfoKid();
//
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> kid = new HashMap<>();
                kid.put("name",edtName.getText().toString()) ;
                kid.put("phone",edtPhone.getText().toString());
                userReference.updateChildren(kid);
            }
        });

        return view;
    }

    private void getInfoKid() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Kid kid = dataSnapshot.getValue(Kid.class);
                // ...
                if (kid!= null){
                    edtName.setText(kid.getName());
                    edtPhone.setText(kid.getPhone());
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
    }
}