package com.example.locationchecker.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.locationchecker.R;

public class TypeLoginFragment extends Fragment {

    private Button btnLogin, btnRegister, btnCode;

    public TypeLoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_type_login, container, false);
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegister = view.findViewById(R.id.btn_register);
        btnCode = view.findViewById(R.id.btn_code);

        //
        final FragmentManager fm = getFragmentManager();
        final FragmentTransaction ft_add = fm.beginTransaction();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft_add.add(R.id.frame_layout, new LoginFragment());
                ft_add.commit();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft_add.add(R.id.frame_layout, new RegisterFragment());
//                ft_add.commit();
            }

        });

        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft_add.add(R.id.frame_layout, new InputCodeFragment());
                ft_add.commit();
            }
        });

        return view;
    }

}