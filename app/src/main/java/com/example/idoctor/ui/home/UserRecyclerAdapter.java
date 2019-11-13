package com.example.idoctor.ui.home;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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


//        User user = mUsers.get(position);
//        holder.mCurrentPosition = position;
//        holder.mTextView.setText(user.getName());
//        Glide
//                .with(mContext)
//                .load(user.getPhotoURL())
//                .into(holder.mImageView);

        holder.setDetails(mContext,mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setDetails(final Context ctx, final User user){
            TextView user_name= mView.findViewById(R.id.textView2);
            ImageView user_image= mView.findViewById(R.id.imageView2);

            user_name.setText(user.getName());

            Glide
                    .with(itemView.getContext())
                    .load(user.getPhotoURL())
                    .into(user_image);
            user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("ALOHA: " + user.getName());
                    Intent intent = new Intent(ctx,ExampleDoctorDetailActivity.class);
                    intent.putExtra("user", user);
                    ctx.startActivity(intent);
                }
            });
        }
    }
}
