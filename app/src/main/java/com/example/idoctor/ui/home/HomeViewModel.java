package com.example.idoctor.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.idoctor.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<User>> mUsers;

    public HomeViewModel() {
        mUsers = new MutableLiveData<>();
        readData();
    }

    public LiveData<List<User>> getText() {
        return mUsers;
    }

    public void readData(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("User");

        Query lastQuery = myRef.orderByChild("role").equalTo(1).limitToLast(4);


        final List<User> users = new ArrayList<>();
        System.out.println("DMMMMMMMMMMMMMMMMMMMMM");
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("CCCCCCCCCCCCCCCCCCCCCCCC");
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+keyNode);
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                    System.out.println("AAAAAAAAAAA: "+user.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("UUUUUUUUUUUUUUUUUUUUUU"+databaseError.toString());
            }
        });
        mUsers.setValue(users);
    }
}