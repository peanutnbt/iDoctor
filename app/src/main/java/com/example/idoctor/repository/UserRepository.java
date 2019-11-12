package com.example.idoctor.repository;

import com.example.idoctor.mapper.UserMapper;
import com.example.idoctor.model.User;
import com.example.idoctor.repository.FirebaseDatabaseRepository;

public class UserRepository extends FirebaseDatabaseRepository<User> {

    public UserRepository() {
        super(new UserMapper());
    }

    @Override
    protected String getRootNode() {
        return "User";
    }
}
