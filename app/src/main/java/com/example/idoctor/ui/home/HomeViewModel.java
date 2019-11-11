package com.example.idoctor.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    public HomeViewModel() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mText = new MutableLiveData<>();
        mText.setValue(mFirebaseUser.getDisplayName());
    }

    public LiveData<String> getText() {
        return mText;
    }
}