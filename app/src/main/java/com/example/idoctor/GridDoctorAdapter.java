package com.example.idoctor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.idoctor.model.User;

import java.util.ArrayList;

public class GridDoctorAdapter extends BaseAdapter {
    private Context context;
    private View view;
    private LayoutInflater layoutInflater;
    private ArrayList<User> users;

    public GridDoctorAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null){
            view = new View(context);
            view = layoutInflater.inflate(R.layout.single_item,null);
            ImageView imageView =  (ImageView) view.findViewById(R.id.imageView2);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("ALOHA: " + users.get(i).getName());

                    Intent ii=new Intent(context, TestActivity.class);
                    ii.putExtra("user", users.get(i));
                    context.startActivity(ii);
                }
            });

            TextView textView =  (TextView) view.findViewById(R.id.textView2);
            textView.setText(users.get(i).getName());
//            imageView.setImageResource(images[i]);
//            String url = "https://firebasestorage.googleapis.com/v0/b/idoctor-f023f.appspot.com/o/Images%20file%2Fa2.jpg?alt=media&token=7c99126c-c9e8-49c0-bc61-2f366be5414c";
            Glide
                    .with(view)
                    .load(users.get(i).getPhotoURL())
                    .into(imageView);
        }
        return view;
    }
}
