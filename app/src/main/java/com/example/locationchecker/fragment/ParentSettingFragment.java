package com.example.locationchecker.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.locationchecker.R;
import com.example.locationchecker.model.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ParentSettingFragment extends Fragment {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private EditText edtName, edtPhone;
    private Button btnUpdate;

    private FirebaseUser firebaseUser;
    private DatabaseReference userReference;
    private String android_id ;
    public ParentSettingFragment() {
        // Required empty public constructor
    }

    private void initView(View view){
        edtName = view.findViewById(R.id.edtname);
        edtPhone = view.findViewById(R.id.edtphone);
        btnUpdate = view.findViewById(R.id.btn_update);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_parent_setting, container, false);
        initView(view);
        android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        getInfoUser();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> parent = new HashMap<>();
                parent.put("name",edtName.getText().toString()) ;
                parent.put("phone",edtPhone.getText().toString());
                userReference.updateChildren(parent);
                Toast.makeText(getContext(),"Thay doi thanh cpng!", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void getInfoUser() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Parent parent = dataSnapshot.getValue(Parent.class);
                // ...
                if (parent!= null){
                    edtName.setText(parent.getName());
                    edtPhone.setText(parent.getPhone());

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