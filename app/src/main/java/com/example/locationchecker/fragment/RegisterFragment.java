package com.example.locationchecker.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.locationchecker.R;
import com.example.locationchecker.model.Parent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import android.provider.Settings.Secure;
public class RegisterFragment extends Fragment {

    private EditText inputName,inputPhone, inputEmail, inputPassword;
    private Button btnRegister, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String firebaseUserId;
    private String android_id ;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUsers;


    public RegisterFragment() {
        // Required empty public constructor
    }

    private void createAccount(final String email, final String password, final String name, final String phone){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAGOK", "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();key
                            //luu du lieu user
                            //tạp userId
                            firebaseUserId = mAuth.getUid();
                            refUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUserId);
//                            DatabaseReference ref = database.getReference();
//                            DatabaseReference parentRef = ref.child("parents");
                            HashMap<String, Object> parent = new HashMap<>();
                            parent.put("id", firebaseUserId);
                            parent.put("email", email);
                            parent.put("name", name);
                            parent.put("phone", phone);
                            parent.put("image", "");
                            parent.put("code", "");
                            refUsers.updateChildren(parent);
//                            parent.put(firebaseUserId, new Parent(firebaseUserId, email, name, phone, "",""));
////                            users.put("gracehop", new Parent("December 9, 1906", "Grace Hopper"));
//
//                            parentRef.setValue(parent);
                            //
                            ArrayList<String> arr = new ArrayList<>();
                            arr.add(email);
                            arr.add(password);
                            LoginFragment nextFrag= new LoginFragment();
                            //
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("key", arr);
                            nextFrag.setArguments(bundle);


                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_layout, nextFrag, "findThisFragment")
                                    .addToBackStack(null)
                                    .commit();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        android_id = Secure.getString(getContext().getContentResolver(),
                Secure.ANDROID_ID);

        //Get Firebase auth instance
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnRegister = (Button) view.findViewById(R.id.btn_register);
        inputName = (EditText) view.findViewById(R.id.edt_name);
        inputPhone = (EditText) view.findViewById(R.id.edt_phone);
        inputEmail = (EditText) view.findViewById(R.id.edt_email);
        inputPassword = (EditText) view.findViewById(R.id.edt_pass);
//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
//        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(inputEmail.getText().toString(),inputPassword.getText().toString(),inputName.getText().toString(), inputPhone.getText().toString());
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }
}