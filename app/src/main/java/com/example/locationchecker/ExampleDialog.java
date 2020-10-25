package com.example.locationchecker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.locationchecker.model.Kid;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExampleDialog extends AppCompatDialogFragment {
    public String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference checkReference;
    DatabaseReference ref = database.getReference();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cảnh bao!!!!")
                .setMessage("Con bạn đang gặp nguy hiểm!")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        checkReference =  ref.child("Kids").child(code);

                        checkReference.child("sos").setValue("false");

                    }
                });

        return builder.create();
    }
}
