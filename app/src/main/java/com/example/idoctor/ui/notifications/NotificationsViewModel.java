package com.example.idoctor.ui.notifications;

import android.util.Log;

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

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<User> mUserMutableLiveData;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;

    private static final String TAG = "ProfileFragment";

    public NotificationsViewModel() {
        mUserMutableLiveData = new MutableLiveData<>();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("User").child(mFirebaseUser.getUid());
        Log.i("TAG HERE",mFirebaseUser.getUid());
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                mUserMutableLiveData.setValue(u);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public LiveData<User> getUser() {
        return mUserMutableLiveData;
    }

    public FirebaseUser getUserURL(){ return mFirebaseUser;}


}