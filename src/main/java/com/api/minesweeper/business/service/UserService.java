package com.api.minesweeper.business.service;

import com.api.minesweeper.data.entity.User;
import com.api.minesweeper.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(String name){

        User user = userRepository.findByName(name);
        if( user == null) {
            user = new User(name);
            userRepository.save(user);
        }
        return user;
    }

    public User findByName(String name){
        return userRepository.findByName(name);
    }
    public Iterable<User> lookup(){
        return userRepository.findAll();
    }
}
