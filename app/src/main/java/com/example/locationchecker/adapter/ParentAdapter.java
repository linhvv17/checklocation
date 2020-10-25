package com.example.locationchecker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locationchecker.R;
import com.example.locationchecker.activity.Chats2Activity;
import com.example.locationchecker.activity.ChatsActivity;
import com.example.locationchecker.model.Kid;
import com.example.locationchecker.model.Parent;

import java.util.ArrayList;

public class ParentAdapter extends
        RecyclerView.Adapter<ParentAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Parent> mParents;

    public ParentAdapter(Context mContext, ArrayList<Parent> mParents) {
        this.mContext = mContext;
        this.mParents = mParents;
    }


    @NonNull
    @Override
    public ParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.item_message, parent, false);
        ParentAdapter.ViewHolder viewHolder = new ParentAdapter.ViewHolder(heroView);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Parent parent = mParents.get(position);
        Glide.with(mContext)
                .load(R.drawable.profile_image)
                .into(holder.mImage);
        holder.mName.setText(parent.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(mContext, Chats2Activity.class);
                chatIntent.putExtra("userid", mParents.get(position).getId());
                mContext.startActivity(chatIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mParents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image_hero);
            mName = itemView.findViewById(R.id.text_name);
        }
    }
}
