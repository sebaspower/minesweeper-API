package com.example.minesweeper.controller;

import com.example.minesweeper.business.service.UserService;
import com.example.minesweeper.data.entity.User;
import com.example.minesweeper.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Iterable<User> get() {
        return userService.lookup();
    }

    @PutMapping
    public User put(@RequestBody User request) {
        if (request != null) {
            return userService.createUser(request.getName());
        }
        return null;
    }

    @GetMapping("/{name}")
    User one(@PathVariable String name) throws Exception {
        return userService.findByName(name);
    }
}
