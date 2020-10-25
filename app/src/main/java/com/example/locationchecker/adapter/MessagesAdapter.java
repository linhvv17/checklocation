package com.example.locationchecker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationchecker.R;
import com.example.locationchecker.model.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private List<Messages> arrMessages;
    private Context context;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    int MSG_TYPE_LEFT = 0;
    int MSG_TYPE_RIGHT = 1;

    public MessagesAdapter(List<Messages> arrMessages, Context context) {
        this.arrMessages = arrMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.message_item_right,parent, false);
            return new ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.message_item_left,parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Messages messages = arrMessages.get(position);
        final String mes = messages.getMessage();
        holder.show_text_message.setText(mes);

    }

    @Override
    public int getItemViewType(int position) {
        if (arrMessages.get(position).getSender() == firebaseUser.getUid()) {
             return  MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return arrMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView show_text_message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_text_message = itemView.findViewById(R.id.show_text_message);
        }
    }
}
