package com.example.idoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.idoctor.model.ChatMessage;
import com.example.idoctor.model.Status;
import com.example.idoctor.model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ExampleDoctorDetailActivity extends AppCompatActivity {

    Button chatNow, chatHistory;
    String doctorID, text1, text2;
    ImageView userImage, nut;
    TextView name, address, email, facebook, twiter, phone, state;



    private FirebaseUser currentUser;
    private String currentUserID;

    private DatabaseReference doctorRef;

    private FirebaseListAdapter<Status> adapter;


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

        if (currentUser != null) {
            updateUserStatus("online");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        if(currentUser != null){
            updateUserStatus("offline");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(currentUser != null){
            updateUserStatus("offline");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_doctor_detail);

        Intent iin= getIntent();
        User user = (User)iin.getSerializableExtra("user");



        chatNow = (Button)findViewById(R.id.chat_button);
        chatHistory = (Button)findViewById(R.id.history_button);

        userImage = findViewById(R.id.imageUser);
        name = findViewById(R.id.tv_name);
        address = findViewById(R.id.tv_address);
        email = findViewById(R.id.email);
        facebook = findViewById(R.id.facebook);
        twiter = findViewById(R.id.twiter);
        phone = findViewById(R.id.phone);
        state = findViewById(R.id.status);
        nut = findViewById(R.id.nut);

        Picasso.get()
                .load(user.getPhotoURL())
                .resize(200, 200)
                .centerCrop()
                .into(userImage);

        name.setText(user.getName());
        address.setText(user.getDescription());
        email.setText(user.getEmail());
        facebook.setText(user.getEmail());
        twiter.setText(user.getEmail());
        phone.setText(user.getUid());



        //handle button Chat
        doctorID = user.getUid();




        chatNow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(v.getContext(),ChatMainActivity.class);
                sendIntent.putExtra("DOCTORID", ""+doctorID);
                startActivity(sendIntent);
            }
        });

        chatHistory.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(v.getContext(),ChatHistoryActivity.class);
                sendIntent.putExtra("DOCTORID", ""+doctorID);
                startActivity(sendIntent);
            }
        });


        //next


        doctorRef = FirebaseDatabase.getInstance().getReference().child("User").child(doctorID);
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        setStateDoctor();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("state")){
                        TextView status;
                        status = (TextView)findViewById(R.id.status);
                        String nutState= dataSnapshot.child("state").getValue().toString();
                        if(nutState.equalsIgnoreCase("online")){
                            nut.setBackgroundColor(Color.parseColor("#8BC34A"));
                        }else{
                            nut.setBackgroundColor(Color.parseColor("#1D350D"));
                        }
                        status.setText(nutState);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setStateDoctor(){
        //get doctorState
        ListView listView = findViewById(R.id.listStatus);

        //get root to query to
        Query query = FirebaseDatabase.getInstance().getReference().child("User")
                .child(doctorID)
                .child("userState")
                .orderByKey()
                .limitToLast(1);
        System.out.println(query);
        //The error said the constructor expected FirebaseListOptions:
        FirebaseListOptions<Status> options = new FirebaseListOptions.Builder<Status>()
                .setQuery(query, Status.class)
                .setLayout(R.layout.status_view)
                .build();

        adapter = new FirebaseListAdapter<Status>(options) {

            @Override
            protected void populateView(@NonNull View v, @NonNull Status model, int position) {
                TextView status;
                status = (TextView) v.findViewById(R.id.status);
                String nutState = model.getState();
                if(nutState.equalsIgnoreCase("online")){
                    nut.setBackgroundColor(Color.parseColor("#8BC34A"));
                }else{
                    nut.setBackgroundColor(Color.parseColor("#1D350D"));
                }
                status.setText(nutState);


            }
        };

        //display all adapter on listView
        listView.setAdapter(adapter);

    }


    private void updateUserStatus(String state){


        String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();



        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("time",saveCurrentTime);
        onlineStateMap.put("date",saveCurrentDate);
        onlineStateMap.put("state",state);

        currentUserID = currentUser.getUid();


        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(currentUserID)
                .child("userState")
                .push()
                .updateChildren(onlineStateMap);

    }
}
