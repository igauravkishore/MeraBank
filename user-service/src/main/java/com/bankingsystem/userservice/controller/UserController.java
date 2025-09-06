package com.bankingsystem.userservice.controller;

import com.bankingsystem.userservice.dto.UserRequest;
import com.bankingsystem.userservice.dto.UserResponse;
import com.bankingsystem.userservice.model.User;
import com.bankingsystem.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping("username/{username}")
    ResponseEntity<Optional<User>> findByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> save(@RequestBody UserRequest userRequest) {
        UserResponse savedUser = userService.createUser(userRequest);  // returns saved entity
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity <User> findById(@PathVariable Long id) {
        return userService.getUserByUserId(id)
                .map(ResponseEntity::ok)
    .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
