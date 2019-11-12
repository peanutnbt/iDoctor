package com.example.idoctor.ui.home;


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
import com.example.idoctor.ExampleDoctorDetailActivity;
import com.example.idoctor.R;
import com.example.idoctor.model.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {


    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<User> mUsers;

    public UserRecyclerAdapter(Context context, List<User> users) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mUsers = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.single_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerAdapter.ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.mCurrentPosition = position;
        holder.mTextView.setText(user.getName());
        Glide
                .with(mContext)
                .load(user.getPhotoURL())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mTextView;
        private int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView2);
            mTextView  = itemView.findViewById(R.id.textView2);

            // set event for a card/item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ExampleDoctorDetailActivity.class);
                    intent.putExtra("user", mUsers.get(mCurrentPosition));
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
