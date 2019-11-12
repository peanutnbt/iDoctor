package com.example.idoctor.mapper;

import com.example.idoctor.domain.UserFirebase;
import com.example.idoctor.model.User;

public class UserMapper extends FirebaseMapper<UserFirebase, User> {

    @Override
    public User map(UserFirebase userFirebase) {
        User user = new User();
        user.setPhotoURL(userFirebase.getPhotoURL());
        user.setEmail(userFirebase.getEmail());
        user.setName(userFirebase.getName());
        user.setRole(userFirebase.getRole());
        user.setUid(userFirebase.getUid());
        user.setRoleName(userFirebase.getRoleName());
        return user;
    }
}
