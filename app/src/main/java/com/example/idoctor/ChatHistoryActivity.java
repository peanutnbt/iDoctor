package com.example.idoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.idoctor.model.ChatMessage;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatHistoryActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main;
    String doctorID;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        //get doctorID from doctor's detail Page
        doctorID = getIntent().getStringExtra("DOCTORID");

        displayChatMessage();


    }

    private void displayChatMessage(){
        ListView listView = findViewById(R.id.listChatHistory);


        //get root to query to
        Query query = FirebaseDatabase.getInstance().getReference().child("Messages")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(doctorID);

        //The error said the constructor expected FirebaseListOptions:
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.list_item)
                .build();


        adapter = new FirebaseListAdapter<ChatMessage>(options) {

            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
                TextView messText, messUser, messTime;
                String messType;
                ImageView profile_image, imageView;

                //get data from firebase databse
                messText = (BubbleTextView) v.findViewById(R.id.message_text);
                messUser = (TextView) v.findViewById(R.id.message_user);
                messTime = (TextView) v.findViewById(R.id.message_time);
                imageView = v.findViewById(R.id.image);
                profile_image = v.findViewById(R.id.profile_image);

                messType = model.getType();

                messUser.setText(model.getMessUser()+"");
                Picasso.get().load(model.getImageUrl()).into(profile_image);

                //if mess is text
                if(messType.equalsIgnoreCase("text")) {
                    messText.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    messText.setText(model.getMessText() + "");


                }else{
                    if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                            .equalsIgnoreCase(messUser.getText()
                                    .toString()))                       {


                        imageView.setVisibility(View.VISIBLE);
                        messText.setVisibility(View.GONE);
                        Picasso.get().load(model.getMessText())
                                .resize(90, 90)
                                .centerCrop()
                                .into(imageView);

                    }
                }

                //format date
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)");
                messTime.setText(""+simpleDateFormat.format(new Date(model.getMessTime())));
            }
        };

        //display all adapter on listView
        listView.setAdapter(adapter);
    }
}
