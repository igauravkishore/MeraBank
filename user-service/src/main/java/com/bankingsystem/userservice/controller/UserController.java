package com.bankingsystem.userservice.controller;

import com.bankingsystem.userservice.model.User;
import com.bankingsystem.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        User savedUser = userService.createUser(user);  // returns saved entity
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/by-keycloak/{keycloakId}")
    public ResponseEntity<User> findByKeycloakId(@PathVariable String keycloakId) {
        User user =  userService.getUserByKeycloakId(keycloakId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity <User> findById(@PathVariable Long id) {
        return userService.getUserByUserId(id)
                .map(ResponseEntity::ok)
    .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        userService.UpdateUser(user);
        return ResponseEntity.ok(user);
    }
}
