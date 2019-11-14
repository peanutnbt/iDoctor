package com.example.idoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.app.IntentService;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.idoctor.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchField;
    private ImageView mSearchBtn;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;

    FirebaseRecyclerAdapter<User,UserViewHolder> firebaseRecyclerAdapter;

    boolean searchAll = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("User");
        mResultList = findViewById(R.id.result_list);
        mSearchBtn = findViewById(R.id.imageView);
        mSearchField = findViewById(R.id.search_field);

        mResultList.setHasFixedSize(false);
        mResultList.setLayoutManager(new LinearLayoutManager(this));



        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);
            }
        });

        TextView search_all_text = findViewById(R.id.findAll_text);
        search_all_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAll = true;
                firebaseUserSearch("");
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseUserSearch("");
    }
    private void firebaseUserSearch(String searchText) {
        Query firebaseQuery;
        if(searchAll){
            searchAll = false;
            firebaseQuery = mUserDatabase.orderByChild("role").equalTo(0);
        }
        else{
            firebaseQuery = mUserDatabase.orderByChild("roleName").equalTo(searchText + "_0");
        }
        System.out.println("here1");

//        Query firebaseQuery= mUserDatabase.orderByChild("role").equalTo(1);
        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(firebaseQuery, User.class)
                        .setLifecycleOwner(this)
                        .build();



        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
                System.out.println("here2");
                holder.setDetails(getApplicationContext(),model);
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                System.out.println("here 4");
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_search_layout, parent, false);

                return new UserViewHolder(view);
            }
        };
        System.out.println("here3");
        mResultList.setAdapter(firebaseRecyclerAdapter);
        System.out.println(mResultList.getAdapter());
        System.out.println("dm3");
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            System.out.println("DM1");
        }
        public void setDetails(final Context ctx, final User user){
            System.out.println("DM2");
            TextView user_name= mView.findViewById(R.id.name_text);
            TextView user_status= mView.findViewById(R.id.status_text);
            ImageView user_image= mView.findViewById(R.id.profile_image);

            user_name.setText(user.getName());
            user_status.setText(user.getName());

            Glide
                    .with(ctx)
                    .load(user.getPhotoURL())
                    .apply(RequestOptions.circleCropTransform())
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
