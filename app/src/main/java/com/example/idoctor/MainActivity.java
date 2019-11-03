package com.example.idoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.idoctor.model.Message;
import com.example.idoctor.model.Room;
import com.example.idoctor.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Room");
//        User u = new User();
//        u.setEmail("ABC");
//        u.setName("Tuan Dung");
//        u.setPhotoURL("dassdas");
//        u.setUid("53jfsdfdhrew");
//        u.setRole(2);
//        myRef.push().setValue(u);
//        Room r = new Room();
//        r.setId("AAAAAAAAAAAAAAa");
//        r.setDoctorUID("BBBBBBBBBBB");
//        r.setUserUID("CCCCCCCCCCCC");
//        Message message = new Message();
//        message.setId("423432");
//        message.setContent("Helo 1");
//        message.setSenderUID("CCCCCCCCCCC");
//        ArrayList<Message> arrayList = new ArrayList<>();
//        arrayList.add(message);
//        arrayList.add(message);
//        arrayList.add(message);
//        arrayList.add(message);
//        r.setMessages(arrayList);
//
//        myRef.push().setValue(r);
    }
}
