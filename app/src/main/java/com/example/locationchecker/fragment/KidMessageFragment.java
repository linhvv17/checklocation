package com.example.locationchecker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationchecker.R;
import com.example.locationchecker.adapter.KidAdapter;
import com.example.locationchecker.adapter.ParentAdapter;
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


public class KidMessageFragment extends Fragment {

    private ArrayList<Parent> mParents ;
    private RecyclerView mRecyclerKidMessage;
    private ParentAdapter mParentAdapter;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser mUser;
    private DatabaseReference kidReference;
    private String code;
    private DatabaseReference userReference;


    public KidMessageFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_kid_message, container, false);

        mRecyclerKidMessage = view.findViewById(R.id.rv_chat_list_kid);
        mParents = new ArrayList<>();
        createHeroList();
        mParentAdapter = new ParentAdapter(getContext(),mParents);
        mRecyclerKidMessage.setAdapter(mParentAdapter);
        mRecyclerKidMessage.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void createHeroList() {
//        mParents.add(new Parent("id","mail","name","phone","image","code"));
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//                mParents.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    mParents.clear();
                    Parent parent = ds.getValue(Parent.class);
                    mParents.add(parent);
//                    if (parent!=null){
//                        code = parent.getCode();
//                        kidReference = FirebaseDatabase.getInstance().getReference().child("Kids").child(code);
//                        kidReference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                Kid kid = snapshot.getValue(Kid.class);
//                                mKidMessages.add(kid);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        userReference.addValueEventListener(postListener);
    }
}