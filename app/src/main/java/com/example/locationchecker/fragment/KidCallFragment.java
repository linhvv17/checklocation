package com.example.locationchecker.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.locationchecker.R;


public class KidCallFragment extends Fragment implements View.OnClickListener{
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private EditText edtNumberPhone;
    private Button btnCallPhone, btnQCallPhone;

    public KidCallFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_kid_call, container, false);
        initView(view);
        setUpView();

        return view;
    }

    private void setUpView() {

        btnCallPhone.setOnClickListener(this);
        btnQCallPhone.setOnClickListener(this);
    }

    private void initView(View view) {
        edtNumberPhone = view.findViewById(R.id.edt_number_kid);
        btnCallPhone = view.findViewById(R.id.btn_call_kid);
        btnQCallPhone = view.findViewById(R.id.btn_quick_call_kid);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_call_kid:
                String number = ("tel:" + edtNumberPhone.getText());
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

            case R.id.btn_quick_call_kid:

                String number2 = ("tel:0398482333");
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