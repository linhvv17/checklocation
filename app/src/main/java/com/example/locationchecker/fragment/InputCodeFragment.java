package com.example.locationchecker.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.locationchecker.R;
import com.example.locationchecker.activity.HomeKidsActivity;
import com.example.locationchecker.activity.HomeParentsActivity;
import com.example.locationchecker.model.Kid;
import com.example.locationchecker.model.Parent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class InputCodeFragment extends Fragment {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String latitude,longitude;
    ArrayList<Parent> parents = new ArrayList<>();
    EditText edtName, edtCode;
    Button btnJoin;
    private String android_id ;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userReference;
    private DatabaseReference refKids;

    private void init(View view){
        edtName = view.findViewById(R.id.edt_name_join);
        edtCode = view.findViewById(R.id.edt_code_join);
        btnJoin = view.findViewById(R.id.btn_join);
    }
    public InputCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //createKid();

    }
    private void checkCode(){
        android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parents.clear();
                for (DataSnapshot d : snapshot.getChildren()){
                    Parent p = d.getValue(Parent.class);
                    parents.add(p);
                }
                for (int i = 0; i< parents.size(); i++){
                    Log.d("OKE", parents.get(i).getCode());
                    if (edtCode.getText().toString().equals(parents.get(i).getCode().toString())){
                        Toast.makeText(getContext(), "Dang nhap thanh cong", Toast.LENGTH_LONG).show();
                        createKid(parents.get(i));
                        addKid();
                        String code = edtCode.getText().toString();
                        Intent intent = new Intent(getActivity().getBaseContext(), HomeKidsActivity.class);
                        intent.putExtra("message", code);
                        getActivity().startActivity(intent);
//                        Intent intent = new Intent(getContext(), HomeKidsActivity.class);
//                        intent.putExtra("code",edtCode.getText().toString());
//                        startActivity(intent);

                    } else {
                        Toast.makeText(getContext(), "Dang nhap that bai", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void createKid(Parent parent) {

        refKids = FirebaseDatabase.getInstance().getReference().child("Users").child(parent.getId()).child("Members").child(edtCode.getText().toString());
        HashMap<String, Object> kid = new HashMap<>();
        kid.put("id", android_id);
        kid.put("type", "kid");
//        kid.put("code", edtCode.getText().toString());
//        kid.put("name", edtName.getText().toString());
//        kid.put("phone", "");
        refKids.updateChildren(kid);
    }



    private void addKid(){
        refKids = FirebaseDatabase.getInstance().getReference().child("Kids").child(edtCode.getText().toString());
        HashMap<String, Object> kid = new HashMap<>();
        kid.put("id", android_id);
        kid.put("code", edtCode.getText().toString());
        kid.put("name", edtName.getText().toString());
        kid.put("phone", "");
        kid.put("lat", "20.980152");//LatLng(-34, 151)
        kid.put("lng", "105.795669");
        kid.put("sos", "false");
        refKids.updateChildren(kid);
    }
    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input_code, container, false);
        init(view);

        android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        userReference = FirebaseDatabase.getInstance().getReference().child("parents");



        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (edtCode.getText().toString().equals("123456")){
//                createKid();
                checkCode();
//                String code = edtCode.getText().toString();
//                Intent intent = new Intent(getActivity().getBaseContext(), HomeKidsActivity.class);
//                intent.putExtra("message", code);
//                getActivity().startActivity(intent);
//                addKid();
//                Intent intent = new Intent(getContext(), HomeKidsActivity.class);
//                startActivity(intent);
//                checkCode();
//                } else {
//                    Toast.makeText(getContext(), "Ma khong hop le", Toast.LENGTH_LONG).show();
//                }
            }
        });
        return view;
    }
}