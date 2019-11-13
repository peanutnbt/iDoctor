package com.example.idoctor.ui.dashboard;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.idoctor.model.ChatMessage;
import com.example.idoctor.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<List<User>> listMutableLiveData;
    private List<User> userArrayList;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;


    public DashboardViewModel() {
        listMutableLiveData = new MutableLiveData();
        final List<String> user_UID_List= new ArrayList();;
        //final List<ChatMessage> chatMessages = new ArrayList<>();;
        userArrayList = new ArrayList<>();
        //Get all UID
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Messages").child(mFirebaseUser.getUid());
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference("User").child(child.getKey());
                    Log.i("Facebook dcm bro123456",child.getKey()+"");
                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User u = dataSnapshot.getValue(User.class);
                            userArrayList.add(u);
//                            GenericTypeIndicator<Map<String, List<User>>> genericTypeIndicator = new GenericTypeIndicator<Map<String, List<User>>>() {};
//                            Map<String, List<User>> hashMap = dataSnapshot.getValue(genericTypeIndicator);
//
//                            for (Map.Entry<String,List<User>> entry : hashMap.entrySet()) {
//                                userArrayList = entry.getValue();
////                                for (User user: users){
////                                }
//                            }
                            listMutableLiveData.setValue(userArrayList);

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                Log.i("I'm fuking here,look at me, bro123456",userArrayList.size()+"");

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        //get All message from UID
//        for(int i =0;i<user_UID_List.size();i++){
//            chatMessages.clear();
//            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Messages").child(mFirebaseUser.getUid()).child(user_UID_List.get(i));
//            Log.i("I'm here",mDatabaseReference.toString());
//            mDatabaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//                        ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
//                        chatMessages.add(chatMessage);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//            temp.put(user_UID_List.get(i),chatMessages);
//            Log.i("get Size",temp.size()+" SIZE HEREEEEEEEE");
//        }


    }
    public LiveData<List<User>> getListMessages(){
        return listMutableLiveData;
    }

}