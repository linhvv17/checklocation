package com.example.locationchecker.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locationchecker.R;
import com.example.locationchecker.adapter.KidAdapter;
import com.example.locationchecker.model.Kid;
import com.example.locationchecker.model.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParentMessageFragment extends Fragment {


    private ArrayList<Kid> mKidMessages ;
    private RecyclerView mRecyclerKidMessage;
    private KidAdapter mkidAdapter;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser mUser;
    private DatabaseReference kidReference;
    private String code;
    private DatabaseReference userReference;


    public ParentMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_message, container, false);

        mRecyclerKidMessage = view.findViewById(R.id.rv_chat_list);
        mKidMessages = new ArrayList<>();
        createHeroList();
        mkidAdapter = new KidAdapter(getContext(),mKidMessages);
        mRecyclerKidMessage.setAdapter(mkidAdapter);
        mRecyclerKidMessage.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void createHeroList() {

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                mKidMessages.clear();
                // Get Post object and use the values to update the UI
                Parent parent = dataSnapshot.getValue(Parent.class);
                // ...
                if (parent!=null){
                    mKidMessages.clear();
                    code = parent.getCode();
                    kidReference = FirebaseDatabase.getInstance().getReference().child("Kids").child(code);
                    kidReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Kid kid = snapshot.getValue(Kid.class);
                            mKidMessages.add(kid);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        userReference.addValueEventListener(postListener);
    }

}