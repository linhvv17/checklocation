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

import java.util.ArrayList;

public class KidAdapter extends
        RecyclerView.Adapter<KidAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Kid> mKidMessages;

    public KidAdapter(Context mContext, ArrayList<Kid> mKidMessages) {
        this.mContext = mContext;
        this.mKidMessages = mKidMessages;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.item_message, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Kid kidMessage = mKidMessages.get(position);
        Glide.with(mContext)
                .load(R.drawable.profile_image)
                .into(holder.mImageHero);
        holder.mTextName.setText(kidMessage.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(mContext, ChatsActivity.class);
                chatIntent.putExtra("userid", kidMessage.getCode());
                mContext.startActivity(chatIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mKidMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageHero;
        private TextView mTextName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageHero = itemView.findViewById(R.id.image_hero);
            mTextName = itemView.findViewById(R.id.text_name);
        }
    }
}
