package com.example.idoctor.repository;

import com.example.idoctor.mapper.FirebaseMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BaseValueEventListener<Model, Entity> implements ValueEventListener {

    private FirebaseMapper<Entity, Model> mapper;
    private FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Model> callback;

    public BaseValueEventListener(FirebaseMapper<Entity, Model> mapper,
                                  FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Model> callback) {
        this.mapper = mapper;
        this.callback = callback;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Model> data = mapper.mapList(dataSnapshot);
        System.out.println("6666666666666666666666: "+data.get(0).toString());
        callback.onSuccess(data);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        System.out.println("777777777777777777777");
        callback.onError(databaseError.toException());
    }
}
