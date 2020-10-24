package com.example.locationchecker.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.locationchecker.R;


public class ParentCallFragment extends Fragment implements View.OnClickListener{
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private EditText edtNumber;
    private Button btnCall, btnQCall;


    public ParentCallFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_parent_call, container, false);
        initView(view);
        setUpView();

        return view;
    }

    private void setUpView() {
        btnCall.setOnClickListener(this);
        btnQCall.setOnClickListener(this);
    }

    private void initView(View view) {
        edtNumber = view.findViewById(R.id.edt_number_phone);
        btnCall = view.findViewById(R.id.btn_call);
        btnQCall = view.findViewById(R.id.btn_quick_call);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_call:
                String number = ("tel:" + edtNumber.getText());
                Intent mIntent = new Intent(Intent.ACTION_CALL);
                mIntent.setData(Uri.parse(number));
// Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(mIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_quick_call:

                String number2 = ("tel:0981838198");
                Intent mIntent2 = new Intent(Intent.ACTION_CALL);
                mIntent2.setData(Uri.parse(number2));
// Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(mIntent2);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
}