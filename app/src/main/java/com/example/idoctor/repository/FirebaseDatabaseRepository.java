package com.example.idoctor.repository;

import com.example.idoctor.mapper.FirebaseMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public abstract class FirebaseDatabaseRepository<Model> {

    protected DatabaseReference databaseReference;
    protected FirebaseDatabaseRepositoryCallback<Model> firebaseCallback;
    private BaseValueEventListener listener;
    private FirebaseMapper mapper;

    protected abstract String getRootNode();

    public FirebaseDatabaseRepository(FirebaseMapper mapper) {
        databaseReference = FirebaseDatabase.getInstance().getReference(getRootNode());
        this.mapper = mapper;
    }

    public void addListener(FirebaseDatabaseRepositoryCallback<Model> firebaseCallback) {
        System.out.println("2222222222222222222");
        this.firebaseCallback = firebaseCallback;
        listener = new BaseValueEventListener(mapper, firebaseCallback);
        System.out.println("333333333333333");
        Query lastQuery = databaseReference.orderByChild("role").equalTo(0).limitToLast(4);
        lastQuery.addValueEventListener(listener);
        System.out.println("444444444444444444");
    }

    public void removeListener() {
        databaseReference.removeEventListener(listener);
    }

    public interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);

        void onError(Exception e);
    }
}