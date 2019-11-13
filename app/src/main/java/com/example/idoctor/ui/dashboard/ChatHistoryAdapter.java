package com.example.idoctor.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.idoctor.ChatHistoryActivity;
import com.example.idoctor.R;
import com.example.idoctor.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<User> users;

    public ChatHistoryAdapter(Context context, List<User> temp) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        users = temp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.chat_history_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDetails(mContext,users.get(position));
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setDetails(final Context ctx, final User user){
            TextView user_name= mView.findViewById(R.id.txt_username);
            TextView description = mView.findViewById(R.id.txt_description);
            CircleImageView circleImageView = mView.findViewById(R.id.avatar_image);

            user_name.setText(makeUserNameLesser(user.getName()));
            Log.i("Hello world with 2 side",user.getEmail()+"");
            description.setText(user.getDescription());
            Glide
                    .with(itemView.getContext())
                    .load(user.getPhotoURL())
                    .into(circleImageView);
//            circleImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(ctx, ChatHistoryActivity.class);
//                    intent.putExtra("user", user);
//                    ctx.startActivity(intent);
//                }
//            });
//            Glide
//                    .with(itemView.getContext())
//                    .load(user.getPhotoURL())
//                    .into(user_image);
//            user_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(ctx, ChatHistoryActivity.class);
//                    intent.putExtra("user", user);
//                    ctx.startActivity(intent);
//                }
//            });
        }
        private String makeUserNameLesser(String username){
            String[] strings = username.split("\\s+");
            int size = strings.length;
            String result = "";
            for(int i = size-1;i<size;i--){
                result += " "+strings[i];
                if(i==size/2){
                    break;
                }
            }
            return result;
        }
    }
}
